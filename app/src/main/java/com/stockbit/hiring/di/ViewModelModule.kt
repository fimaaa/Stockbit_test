package com.stockbit.hiring.di

import com.stockbit.hiring.ui.crypto.list.ListCryptoViewModel
import com.stockbit.hiring.ui.crypto.socket.SocketCryptoViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ListCryptoViewModel(get(named("webSocket")), get()) }
    viewModel { SocketCryptoViewModel(get(named("webSocket"))) }
}

