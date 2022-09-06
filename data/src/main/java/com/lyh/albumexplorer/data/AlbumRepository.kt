package com.lyh.albumexplorer.data

import com.lyh.albumexplorer.data.core.AppDispatchers
import com.lyh.albumexplorer.data.core.fetchAndStoreLocally
import com.lyh.albumexplorer.data.local.dao.AlbumDao
import com.lyh.albumexplorer.data.mapper.toEntities
import com.lyh.albumexplorer.data.mapper.toModel
import com.lyh.albumexplorer.data.mapper.toModels
import com.lyh.albumexplorer.data.remote.AlbumApi
import com.lyh.albumexplorer.data.remote.dto.AlbumDto
import com.lyh.albumexplorer.domain.core.Result
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.domain.repository.IAlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class AlbumRepository(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val dispatchers: AppDispatchers
) : IAlbumRepository {

    private var lastFetchAlbums: Date? = null

    override fun getAlbumById(id: Long): Flow<Result<AlbumModel>> =
        flow<Result<AlbumModel>> {
            emit(ResultSuccess(albumDao.getAlbumById(id).toModel()))
        }.catch {
            emit(ResultException(it))
        }.flowOn(dispatchers.io)

    override fun getAlbums(): Flow<Result<List<AlbumModel>>> = fetchAndStoreLocally(
        ::getRemoteAlbums,
        ::syncAlbums,
        ::getLocalAlbums,
        lastFetchAlbums,
        ::fetchRemoteSuccessCallback
    ).catch {
        emit(ResultException(it))
    }.flowOn(dispatchers.io)

    private fun fetchRemoteSuccessCallback() {
        lastFetchAlbums = Date()
    }

    private suspend fun getRemoteAlbums() = albumApi.getAlbums()

    private suspend fun getLocalAlbums() = albumDao.getAlbums().toModels()

    private suspend fun syncAlbums(albumsFromRemote: List<AlbumDto>): List<AlbumModel> {
        // add from remote
        val albumsToSave = albumsFromRemote.toEntities()

        albumDao.insertOrUpdateAlbums(albumsToSave)

        val storedAlbums = albumDao.getAlbums()

        // check if there is some albums to delete in database (present in db but not from API)
        val albumsToDelete =
            storedAlbums.filter { stored -> albumsToSave.none { it.id == stored.id } }

        if (albumsToDelete.isNotEmpty()) {
            // delete albums that were in database but are not in the result got from the API
            albumDao.deleteAlbums(albumsToDelete)
        }

        return albumsToSave.toModels()
    }
}

