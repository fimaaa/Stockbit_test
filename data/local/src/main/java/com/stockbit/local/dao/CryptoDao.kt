package com.stockbit.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.stockbit.model.crypto.ResponseListCryptoInfo

@Dao
abstract class CryptoDao: BaseDao<ResponseListCryptoInfo>() {

    @Query("SELECT * FROM ResponseListCryptoInfo WHERE id = :id LIMIT 1")
    abstract suspend fun getExample(id: Int): ResponseListCryptoInfo

    suspend fun save(data: ResponseListCryptoInfo) {
        insert(data)
    }

    suspend fun save(datas: List<ResponseListCryptoInfo>) {
        insert(datas)
    }
}