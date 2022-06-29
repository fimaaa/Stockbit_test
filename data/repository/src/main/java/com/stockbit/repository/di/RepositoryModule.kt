package com.stockbit.repository.di

import com.stockbit.repository.AppDispatchers
import com.stockbit.repository.TopListRepository
import com.stockbit.repository.TopListRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    factory<TopListRepository> { TopListRepositoryImpl(get(), get()) }
}