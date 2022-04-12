package com.abdullah996.stockmarketapp.domain.model

import java.time.LocalDateTime

data class IntraDayInfo (
    val date:LocalDateTime,
    val close:Double
)