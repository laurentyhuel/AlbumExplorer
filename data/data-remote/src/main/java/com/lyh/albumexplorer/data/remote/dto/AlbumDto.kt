package com.lyh.albumexplorer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val albumId: Long,
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)