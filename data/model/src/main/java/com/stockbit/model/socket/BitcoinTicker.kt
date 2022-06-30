package com.stockbit.model.socket

import com.google.gson.annotations.SerializedName

data class BitcoinTicker(
    @SerializedName("price")
    val price: String? = ""
)
