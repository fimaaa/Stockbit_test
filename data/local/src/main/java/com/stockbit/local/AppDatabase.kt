package com.stockbit.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stockbit.local.converter.Converters
import com.stockbit.local.dao.CryptoDao
import com.stockbit.local.dao.RemoteKeysDao
import com.stockbit.model.crypto.RemoteKeys
import com.stockbit.model.crypto.ResponseListCryptoInfo

@Database(entities = [ResponseListCryptoInfo::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    // DAO
    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun cryptoDao(): CryptoDao

    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "App.db")
                .build()
    }
}