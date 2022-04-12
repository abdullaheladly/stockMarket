package com.abdullah996.stockmarketapp.data.repository

import com.abdullah996.stockmarketapp.data.csv.CSVParser
import com.abdullah996.stockmarketapp.data.csv.CompanyListingsParser
import com.abdullah996.stockmarketapp.data.csv.IntraDayInfoParser
import com.abdullah996.stockmarketapp.data.local.StockDatabase
import com.abdullah996.stockmarketapp.data.mapper.toCompanyInfo
import com.abdullah996.stockmarketapp.data.mapper.toCompanyListing
import com.abdullah996.stockmarketapp.data.mapper.toCompanyListingEntity
import com.abdullah996.stockmarketapp.data.remote.StockApi
import com.abdullah996.stockmarketapp.domain.model.CompanyInfo
import com.abdullah996.stockmarketapp.domain.model.CompanyListing
import com.abdullah996.stockmarketapp.domain.model.IntraDayInfo
import com.abdullah996.stockmarketapp.domain.repository.StockRepository
import com.abdullah996.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StockRepositoryImpl @Inject constructor(
   private val api:StockApi,
   private val db:StockDatabase,
   private val companyListingsParser: CSVParser<CompanyListing>,
   private val intraDayInfoParser: CSVParser<IntraDayInfo>
):StockRepository {

    private val dao=db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings=dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))
            val isDbEmpty=localListings.isEmpty()&&query.isBlank()
            val shouldJustLoadFromCache=!isDbEmpty&&!fetchFromRemote
            if (shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListing=try {
                val response =api.getListings()
                companyListingsParser.parse(response.byteStream())
            }catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }catch (e:HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }
            remoteListing?.let {listings->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map {
                        it.toCompanyListingEntity()
                    }
                )
                emit(Resource.Loading(false))
                emit(Resource.Success(
                    data = dao.searchCompanyListing("").map {
                        it.toCompanyListing()
                    }
                ))

            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            val response=api.getIntraDayInfo(symbol)
            val results=intraDayInfoParser.parse(response.byteStream())
            Resource.Success(results)

        }catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraDay info"
            )
        }catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraDay info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result=api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        }catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }
    }
}