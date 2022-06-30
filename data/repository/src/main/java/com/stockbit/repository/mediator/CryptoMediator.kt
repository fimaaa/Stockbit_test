package com.stockbit.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.stockbit.local.AppDatabase
import com.stockbit.local.dao.CryptoDao
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.remote.TopListDataSource
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CryptoMediator(
    private val service: TopListDataSource,
    private val dao: CryptoDao
): RemoteMediator<Int, ResponseListCryptoInfo>() {

    private var page = -1

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ResponseListCryptoInfo>
    ): MediatorResult {
        page = if(page < 0) 0 else page+1
//        when (loadType) {
//            LoadType.REFRESH -> …
//                LoadType.PREPEND -> …
//                LoadType.APPEND -> …
//        }
        try {
            val apiResponse = service.fetchTopUsersAsync(page)

            val repos = apiResponse.data
            repos.map {
                it.id = it.coinInfo.id
            }
            val endOfPaginationReached = (apiResponse.pagination?.totalPage ?: 0) <= (page + 1) * 50
//            repoDatabase.withTransaction {
//                repoDatabase.reposDao().insertAll(repos)
//            }
            dao.save(repos)
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            page -= 1
            println("TAG ERROR MEDIATOR => $exception")
            return MediatorResult.Error(exception)
        }
    }
}
