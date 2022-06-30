package com.stockbit.remote.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.stockbit.remote.TopListDataSource
import com.stockbit.remote.TopListServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
private const val BASE_URL_TOPTIER = "https://min-api.cryptocompare.com/data/"
private const val BASIC_AUTH = "93a3c9336dee6d5b6545f45e717507990e339780998e33ccaa4aa75625a6e661"

val createRemoteModule = module {



    factory<Interceptor> {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    factory<OkHttpClient> {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .addInterceptor(get<Interceptor>())
            .addInterceptor { chain ->
                val language = if (Locale.getDefault().language == "in") "id" else "en"
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                    .addHeader(
                        "Authorization", BASIC_AUTH
                    )
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Language", language)
                    .build()
                chain.proceed(requestBuilder)
            }
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
        okHttpClientBuilder.build()
    }

    single<Retrofit>(named("topTierRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BASE_URL_TOPTIER)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory(named("topTier")){ get<Retrofit>(named("topTierRetrofit")).create(TopListServices::class.java) }
    factory { TopListDataSource(get(named("topTier"))) }
}