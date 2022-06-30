package com.stockbit.model.crypto

import com.google.gson.annotations.SerializedName

data class CryptoValue (
    @SerializedName("PRICE")
    val price: String = "",

    @SerializedName("CHANGE24HOUR")
    val change: String = ""


)