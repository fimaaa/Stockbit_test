package com.stockbit.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.stockbit.local.dao.CryptoDao
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.model.common.BaseResponse
import com.stockbit.remote.TopListDataSource
import com.stockbit.repository.mediator.CryptoMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TopListRepository {
    suspend fun getListTopTier(): Flow<BaseResponse<String, List<ResponseListCryptoInfo>>>

    suspend fun getListTopTierLocal(): List<ResponseListCryptoInfo>

    fun getPagingTopTier(): LiveData<PagingData<ResponseListCryptoInfo>>

    fun getPagingTopTierLocal(): PagingSource<Int, ResponseListCryptoInfo>
}

class TopListRepositoryImpl(
    private val datasource: TopListDataSource,
    private val dao: CryptoDao
): TopListRepository {

    override suspend fun getListTopTier(): Flow<BaseResponse<String, List<ResponseListCryptoInfo>>> {
        return flow {
                    val data = datasource.fetchTopUsersAsync()
                    emit(data)
            }
    }

    override suspend fun getListTopTierLocal(): List<ResponseListCryptoInfo> = dao.getExample()

    override fun getPagingTopTier(): LiveData<PagingData<ResponseListCryptoInfo>> =
        @OptIn(ExperimentalPagingApi::class)
        Pager(
            config = PagingConfig(
                pageSize = 50,
                maxSize = 200,
                enablePlaceholders = false,
                prefetchDistance = 10
            ),
            pagingSourceFactory = { getPagingTopTierLocal() },
            remoteMediator = CryptoMediator(
                datasource,
                dao
            )
        // CryptoPagingSource(datasource)
        ).liveData

    override fun getPagingTopTierLocal(): PagingSource<Int, ResponseListCryptoInfo> = dao.getListCryptoPagination()
}