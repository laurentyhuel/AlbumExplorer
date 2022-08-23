package com.lyh.albumexplorer.domain.di

import com.lyh.albumexplorer.domain.AlbumUseCase
import org.koin.dsl.module

/**
 * Koin module for data-remote
 */
val domainModule = module {
    single { AlbumUseCase(get()) }
}