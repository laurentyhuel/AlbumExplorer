package com.lyh.albumexplorer.domain.model

data class AlbumModel(
    val albumId: Long,
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)