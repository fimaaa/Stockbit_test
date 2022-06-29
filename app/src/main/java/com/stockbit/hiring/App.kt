package com.stockbit.hiring

import android.app.Application
import com.stockbit.hiring.di.appComponent
import com.stockbit.hiring.di.viewModelModule
import com.stockbit.local.di.localModule
import com.stockbit.remote.di.createRemoteModule
import com.stockbit.repository.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class App: Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            androidContext(this@App)
//            provideComponent()
            modules(
                listOf(
                    viewModelModule, createRemoteModule, repositoryModule, localModule
                )
            )
        }

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}