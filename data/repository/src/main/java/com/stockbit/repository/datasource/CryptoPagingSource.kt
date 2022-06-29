package com.stockbit.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.remote.TopListDataSource

private const val STARTING_PAGE_INDEX = 0

class CryptoPagingSource(
    private val topListServices: TopListDataSource,
) : PagingSource<Int, ResponseListCryptoInfo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseListCryptoInfo> {
        return try {
            val position = params.key ?: STARTING_PAGE_INDEX

            val list = mutableListOf<ResponseListCryptoInfo>()
            val response = topListServices.fetchTopUsersAsync(page = position)
            response.data.let { list.addAll(it) }

            LoadResult.Page(
                data = list,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
//                nextKey = if (response.isNullOrEmpty()) null else position + 1
                nextKey = if ((response.pagination?.totalPage ?: 0) > 50 * (position + 1)) position +1 else null
            )
        } catch (e: Exception) {
            println("TAG ERROR $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResponseListCryptoInfo>): Int = STARTING_PAGE_INDEX
}