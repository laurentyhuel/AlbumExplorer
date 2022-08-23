package com.lyh.albumexplorer.data.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lyh.albumexplorer.data.remote.AlbumApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Koin module for data-remote
 */
fun getDataRemoteModule(isDebugEnabled: Boolean) = module {

    // Retrofit and OkHttp setup
    single { Json { ignoreUnknownKeys = true } }
    single { providesOkHttpClient(isDebugEnabled) }
    single { providesRetrofit(get(), get()) }

    // API
    single { providesAlbumApi(get()) }
}

private fun providesAlbumApi(retrofit: Retrofit): AlbumApi = retrofit.create(AlbumApi::class.java)

private fun providesRetrofit(jsonSerializer: Json, okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(lbcBaseUrl)
    .client(okHttpClient)
    .addConverterFactory(jsonSerializer.asConverterFactory(mediaTypeJson.toMediaType()))
    .build()

private fun providesOkHttpClient(isDebugEnabled: Boolean): OkHttpClient = OkHttpClient()
    .newBuilder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (isDebugEnabled)
            Level.BODY
        else
            Level.NONE
    })
    .build()

private const val lbcBaseUrl = "https://mocki.io/v1/"
private const val mediaTypeJson = "application/json"




