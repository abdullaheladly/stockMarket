package com.abdullah996.stockmarketapp.data.repository

import com.abdullah996.stockmarketapp.data.local.StockDatabase
import com.abdullah996.stockmarketapp.data.mapper.toCompanyListing
import com.abdullah996.stockmarketapp.data.remote.StockApi
import com.abdullah996.stockmarketapp.domain.model.CompanyListing
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
    val api:StockApi,
    val db:StockDatabase
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
                response.byteStream()
            }catch (e:IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }catch (e:HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }
}