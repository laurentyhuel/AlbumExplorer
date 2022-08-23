package com.lyh.albumexplorer.domain.repository

import com.lyh.albumexplorer.domain.core.Result
import com.lyh.albumexplorer.domain.model.AlbumModel
import kotlinx.coroutines.flow.Flow

interface IAlbumRepository {
    fun getAlbums(): Flow<Result<List<AlbumModel>>>

    suspend fun getAlbumById(id: Long): Flow<Result<AlbumModel>>
}
