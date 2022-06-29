package com.stockbit.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.stockbit.model.CryptoData

@Dao
abstract class ExampleDao: BaseDao<CryptoData>() {

    @Query("SELECT * FROM CryptoData WHERE name = :name LIMIT 1")
    abstract suspend fun getExample(name: String): CryptoData

    suspend fun save(data: CryptoData) {
        insert(data)
    }

    suspend fun save(datas: List<CryptoData>) {
        insert(datas)
    }
}