package com.stockbit.model.socket

import com.google.gson.annotations.SerializedName

data class BitcoinTicker(
    @SerializedName("TYPE")
    val type: Int = -1,
    @SerializedName("SYMBOL")
    val symbol: String? = "",
    @SerializedName("TOPTIERFULLVOLUME")
    val topTierVolume: Double? = 0.0
)
