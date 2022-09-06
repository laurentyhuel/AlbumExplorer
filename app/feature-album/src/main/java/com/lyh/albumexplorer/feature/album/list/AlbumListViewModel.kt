package com.lyh.albumexplorer.feature.album.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyh.albumexplorer.domain.AlbumUseCase
import com.lyh.albumexplorer.domain.core.ResultError
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.feature.album.mapper.toUis
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class AlbumListViewModel(
    private val albumUseCase: AlbumUseCase
) : ViewModel() {

    private val albumsTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)

    init {
        triggerAlbums()
    }

    val albums: StateFlow<Resource<List<AlbumUi>>> =
        albumsTrigger.flatMapLatest {
            getAlbumsFlow()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResourceLoading()
        )

    private fun getAlbumsFlow(): Flow<Resource<List<AlbumUi>>> = albumUseCase.getAlbums()
        .map {
            //TODO simulate call time
            delay(500)
            when (it) {
                is ResultSuccess -> ResourceSuccess(it.data.toUis())
                is ResultError -> {
                    Timber.e("Failed to getAlbums code=${it.code} message=${it.message}")
                    ResourceError(
                        errorMessage = ErrorMessage.ErrorMessageString(
                            it.message
                        )
                    )
                }
                is ResultException -> {
                    Timber.e(it.throwable, "Error when getAlbums")
                    ResourceError(
                        errorMessage = ErrorMessage.ErrorMessageResource(
                            R.string.fetch_exception
                        )
                    )
                }
            }
        }
        .onStart {
            emit(ResourceLoading())
        }

    fun triggerAlbums() = viewModelScope.launch {
        albumsTrigger.emit(Unit)
    }
}