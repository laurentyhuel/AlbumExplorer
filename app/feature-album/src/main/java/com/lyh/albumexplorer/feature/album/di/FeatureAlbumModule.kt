package com.lyh.albumexplorer.feature.album.di

import com.lyh.albumexplorer.feature.album.detail.AlbumViewModel
import com.lyh.albumexplorer.feature.album.list.AlbumListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureAlbumModule = module {
    viewModel { AlbumViewModel(get()) }
    viewModel { AlbumListViewModel(get()) }
}