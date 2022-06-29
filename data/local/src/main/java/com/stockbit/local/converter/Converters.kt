package com.stockbit.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.stockbit.model.crypto.CryptoData
import com.stockbit.model.crypto.CryptoValue
import com.stockbit.model.crypto.ResponseListCryptoInfo

class Converters {
//    @TypeConverter
//    fun fromTimestamp(value: Long?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: Date?): Long? {
//        return date?.time
//    }

    @TypeConverter
    fun listCryptoDataToJson(value: List<ResponseListCryptoInfo>): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToListCryptoData(value: String) =
        Gson().fromJson(value, Array<ResponseListCryptoInfo>::class.java).toList()

    @TypeConverter
    fun moneyToJson(value: ResponseListCryptoInfo.MoneyData): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToMoney(value: String) =
        Gson().fromJson(value, ResponseListCryptoInfo.MoneyData::class.java)

    @TypeConverter
    fun cryptoDataToJson(value: CryptoData): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToCryptoData(value: String) =
        Gson().fromJson(value, CryptoData::class.java)

    @TypeConverter
    fun cryptoValueToJson(value: CryptoValue): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToCryptoValue(value: String) =
        Gson().fromJson(value, CryptoValue::class.java)



}