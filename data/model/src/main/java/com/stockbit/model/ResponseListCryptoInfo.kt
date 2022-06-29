package com.stockbit.model

import com.google.gson.annotations.SerializedName

data class ResponseListCryptoInfo(
    @SerializedName("CoinInfo")
    val coinInfo: CryptoData,
)
