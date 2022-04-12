package com.abdullah996.stockmarketapp.di

import com.abdullah996.stockmarketapp.data.csv.CSVParser
import com.abdullah996.stockmarketapp.data.csv.CompanyListingsParser
import com.abdullah996.stockmarketapp.data.csv.IntraDayInfoParser
import com.abdullah996.stockmarketapp.data.repository.StockRepositoryImpl
import com.abdullah996.stockmarketapp.domain.model.CompanyListing
import com.abdullah996.stockmarketapp.domain.model.IntraDayInfo
import com.abdullah996.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ):CSVParser<CompanyListing>
    @Binds
    @Singleton
    abstract fun bindCIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ):CSVParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ):StockRepository
}