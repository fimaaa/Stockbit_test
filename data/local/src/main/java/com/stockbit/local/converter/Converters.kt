package com.stockbit.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.stockbit.model.CryptoData
import java.util.*

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
    fun ratingToJson(value: CryptoData.Rating): String =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToRating(value: String) =
        Gson().fromJson(value, CryptoData.Rating::class.java)
}