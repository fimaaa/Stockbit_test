package com.stockbit.repository

import com.stockbit.local.dao.ExampleDao
import com.stockbit.model.CryptoData
import com.stockbit.remote.ExampleDatasource
import com.stockbit.repository.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ExampleRepository {
    suspend fun getExample(): Flow<Resource<CryptoData>>
}

class ExampleRepositoryImpl(private val datasource: ExampleDatasource,
                            private val dao: ExampleDao): ExampleRepository {

    override suspend fun getExample(): Flow<Resource<CryptoData>> {
        return flow {  }
    }

}