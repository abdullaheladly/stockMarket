package com.abdullah996.stockmarketapp.data.mapper

import com.abdullah996.stockmarketapp.data.local.CompanyListingEntity
import com.abdullah996.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.abdullah996.stockmarketapp.domain.model.CompanyInfo
import com.abdullah996.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing():CompanyListing{
    return CompanyListing(name = name,symbol = symbol,exchange = exchange)
}
fun CompanyListing.toCompanyListingEntity():CompanyListingEntity{
    return CompanyListingEntity(name = name,symbol = symbol,exchange = exchange)
}
fun CompanyInfoDto.toCompanyInfo():CompanyInfo{
    return CompanyInfo(
        symbol = symbol?:"",
        description = description?:"",
        name = name?:"",
        country = country?:"",
        industry = industry?:"",
    )
}

