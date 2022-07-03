package com.stockbit.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.stockbit.model.crypto.ResponseListCryptoInfo

@Dao
abstract class CryptoDao: BaseDao<ResponseListCryptoInfo>() {

    @Query("SELECT * FROM ResponseListCryptoInfo ORDER BY page ASC")
    abstract suspend fun getAllListCrypto(): List<ResponseListCryptoInfo>

    @Query("SELECT * FROM ResponseListCryptoInfo ORDER BY page ASC LIMIT 1")
    abstract suspend fun checkListExist(): ResponseListCryptoInfo?

    @Query("SELECT * FROM ResponseListCryptoInfo ORDER BY page ASC")
    abstract fun getListCryptoPagination(): PagingSource<Int, ResponseListCryptoInfo>


    @Query("DELETE FROM ResponseListCryptoInfo")
    abstract suspend fun deleteAll()

    suspend fun save(data: ResponseListCryptoInfo) {
        insert(data)
    }

    suspend fun save(datas: List<ResponseListCryptoInfo>) {
        insert(datas)
    }
}