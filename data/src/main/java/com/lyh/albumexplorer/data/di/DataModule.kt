package com.lyh.albumexplorer.data.di

import com.lyh.albumexplorer.data.AlbumRepository
import com.lyh.albumexplorer.data.core.AppDispatchers
import com.lyh.albumexplorer.data.local.di.dataLocalModule
import com.lyh.albumexplorer.data.remote.di.getDataRemoteModule
import com.lyh.albumexplorer.domain.repository.IAlbumRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * Koin module for data
 */
fun getDataModule(isDebugEnabled: Boolean) = module {
    includes(getDataRemoteModule(isDebugEnabled), dataLocalModule)

    single<IAlbumRepository> { AlbumRepository(get(), get(), get()) }
    single { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
}
