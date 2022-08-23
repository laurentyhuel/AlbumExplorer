package com.lyh.albumexplorer.feature.album.mapper

import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.feature.album.model.AlbumUi

fun AlbumModel.toUi() = AlbumUi(
    this.id,
    this.title,
    this.url,
    this.thumbnailUrl
)

fun List<AlbumModel>.toUis() = this.map { it.toUi() }