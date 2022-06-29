package com.stockbit.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.stockbit.local.dao.CryptoDao
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.model.common.BaseResponse
import com.stockbit.remote.TopListDataSource
import com.stockbit.repository.datasource.CryptoPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TopListRepository {
    suspend fun getListTopTier(): Flow<BaseResponse<String, List<ResponseListCryptoInfo>>>

    fun getPagingTopTier(): LiveData<PagingData<ResponseListCryptoInfo>>
}

class TopListRepositoryImpl(private val datasource: TopListDataSource,
                            private val dao: CryptoDao): TopListRepository {

//    override suspend fun getListTopTier(): Flow<Resource<CryptoData>> {
override suspend fun getListTopTier(): Flow<BaseResponse<String, List<ResponseListCryptoInfo>>> {
    return flow {
                val data = datasource.fetchTopUsersAsync()
                emit(data)
        }
    }

    override fun getPagingTopTier(): LiveData<PagingData<ResponseListCryptoInfo>> =
        Pager(
            config = PagingConfig(
                pageSize = 50,
                maxSize = 200,
                enablePlaceholders = false,
                prefetchDistance = 10
            ),
            pagingSourceFactory = { CryptoPagingSource(datasource) }
        ).liveData
}