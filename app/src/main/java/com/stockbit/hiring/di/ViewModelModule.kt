package com.stockbit.hiring.di

import com.stockbit.hiring.ui.crypto.list.ListCryptoViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ListCryptoViewModel(get()) }
}

