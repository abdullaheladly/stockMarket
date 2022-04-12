package com.abdullah996.stockmarketapp.presentation.company_info

import com.abdullah996.stockmarketapp.domain.model.CompanyInfo
import com.abdullah996.stockmarketapp.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfo:List<IntraDayInfo> = emptyList(),
    val company:CompanyInfo?=null,
    val isLoading:Boolean=false,
    val error:String?=null
)
