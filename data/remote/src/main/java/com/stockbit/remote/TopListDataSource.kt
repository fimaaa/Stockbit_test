package com.stockbit.remote

/**
 * Implementation of [TopListServices] interface
 */
class TopListDataSource(private val topListServices: TopListServices) {

    suspend fun fetchTopUsersAsync(page: Int = 0) =
            topListServices.getTotalTopTierAsync(page = page)

}