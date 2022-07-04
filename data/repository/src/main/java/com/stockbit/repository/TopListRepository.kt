package com.stockbit.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.stockbit.local.dao.CryptoDao
import com.stockbit.local.dao.RemoteKeysDao
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.model.common.BaseResponse
import com.stockbit.remote.TopListDataSource
import com.stockbit.repository.mediator.CryptoMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.java_websocket.client.WebSocketClient

interface TopListRepository {
    suspend fun getListTopTier(): Flow<BaseResponse<String, List<ResponseListCryptoInfo>>>

    suspend fun getListTopTierLocal(): List<ResponseListCryptoInfo>

    suspend fun checkDaoExist() : Boolean

    suspend fun clearAllData()

    fun getPagingTopTier(webSocketClient: WebSocketClient): LiveData<PagingData<ResponseListCryptoInfo>>

    fun getPagingTopTierLocal(): PagingSource<Int, ResponseListCryptoInfo>
}

class TopListRepositoryImpl(
    private val datasource: TopListDataSource,
    private val dao: CryptoDao,
    private val remoteKeyDao: RemoteKeysDao
): TopListRepository {

    override suspend fun getListTopTier(): Flow<BaseResponse<String, List<ResponseListCryptoInfo>>> {
        return flow {
                    val data = datasource.fetchTopUsersAsync()
                    emit(data)
            }
    }

    override suspend fun getListTopTierLocal(): List<ResponseListCryptoInfo> = dao.getAllListCrypto()

    override suspend fun checkDaoExist() = dao.checkListExist () != null


    override suspend fun clearAllData() {
        dao.deleteAll()
        remoteKeyDao.clearRemoteKeys()
    }

    override fun getPagingTopTier(webSocketClient: WebSocketClient): LiveData<PagingData<ResponseListCryptoInfo>> =
        @OptIn(ExperimentalPagingApi::class)
        Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { getPagingTopTierLocal() },
            remoteMediator = CryptoMediator(
                service = datasource,
                dao = dao,
                remoteKeyDao = remoteKeyDao,
                webSocketClient = webSocketClient
            )
            // CryptoPagingSource(datasource)
        ).liveData

    override fun getPagingTopTierLocal(): PagingSource<Int, ResponseListCryptoInfo> = dao.getListCryptoPagination()

}