package com.abdullah996.stockmarketapp.domain.repository

import com.abdullah996.stockmarketapp.domain.model.CompanyListing
import com.abdullah996.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote:Boolean,
        query:String
    ):Flow<Resource<List<CompanyListing>>>
}