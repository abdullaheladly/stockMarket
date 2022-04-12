package com.abdullah996.stockmarketapp.data.mapper

import com.abdullah996.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.abdullah996.stockmarketapp.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntraDayInfoDto.toIntraDayInfo():IntraDayInfo{
    val pattern="yyyy-MM-dd HH:mm:ss"
    val formatter=DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localeDateTime=LocalDateTime.parse(timeStamp,formatter)
    return IntraDayInfo(
        date = localeDateTime,
        close = close
    )

}