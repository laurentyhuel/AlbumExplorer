package com.lyh.albumexplorer.domain

import com.lyh.albumexplorer.domain.core.Result
import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.domain.repository.IAlbumRepository
import kotlinx.coroutines.flow.Flow

class AlbumUseCase(private val albumRepository: IAlbumRepository) {

    fun getAlbums() = albumRepository.getAlbums()

    suspend fun getAlbumById(id: Long): Flow<Result<AlbumModel>> = albumRepository.getAlbumById(id)
}
