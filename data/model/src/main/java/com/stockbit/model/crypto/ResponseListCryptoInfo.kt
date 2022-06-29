package com.stockbit.model.crypto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ResponseListCryptoInfo(
    @PrimaryKey
    var id : Int = 0,

    @SerializedName("CoinInfo")
    val coinInfo: CryptoData,

    @SerializedName("DISPLAY")
    val moneyData: MoneyData
) {
    data class MoneyData(
        @SerializedName("USD")
        val coinValue: CryptoValue
    )
}
