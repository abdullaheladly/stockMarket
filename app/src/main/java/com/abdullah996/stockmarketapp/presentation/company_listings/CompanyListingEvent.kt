package com.abdullah996.stockmarketapp.presentation.company_listings

sealed class CompanyListingEvent {
    object Refresh:CompanyListingEvent()
    data class OnSearchQueryChanged(val query:String):CompanyListingEvent()
}