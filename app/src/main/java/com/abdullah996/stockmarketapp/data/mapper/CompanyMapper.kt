package com.abdullah996.stockmarketapp.data.mapper

import com.abdullah996.stockmarketapp.data.local.CompanyListingEntity
import com.abdullah996.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing():CompanyListing{
    return CompanyListing(name = name,symbol = symbol,exchange = exchange)
}
fun CompanyListing.toCompanyListingEntity():CompanyListingEntity{
    return CompanyListingEntity(name = name,symbol = symbol,exchange = exchange)
}

