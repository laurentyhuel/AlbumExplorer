package com.lyh.albumexplorer.data.mapper

import com.lyh.albumexplorer.data.local.entity.AlbumEntity
import com.lyh.albumexplorer.data.remote.dto.AlbumDto
import com.lyh.albumexplorer.domain.model.AlbumModel

internal fun List<AlbumDto>.toEntities() = this.map { it.toEntity() }

internal fun AlbumDto.toEntity() = AlbumEntity(
    this.albumId,
    this.id,
    this.title,
    this.url,
    this.thumbnailUrl
)

internal fun List<AlbumEntity>.toModels() = this.map { it.toModel() }

internal fun AlbumEntity.toModel() = AlbumModel(
    this.albumId,
    this.id,
    this.title,
    this.url,
    this.thumbnailUrl
)

