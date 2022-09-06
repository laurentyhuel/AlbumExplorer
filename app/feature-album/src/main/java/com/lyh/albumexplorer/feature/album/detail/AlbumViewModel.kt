package com.lyh.albumexplorer.feature.album.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyh.albumexplorer.domain.AlbumUseCase
import com.lyh.albumexplorer.domain.core.ResultError
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.feature.album.mapper.toUi
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class AlbumViewModel(
//    savedStateHandle: SavedStateHandle,
    private val albumUseCase: AlbumUseCase
) : ViewModel() {

    //TODO cannot use SavedStateHandle https://github.com/InsertKoinIO/koin/issues/1350
//    private val albumId: String = checkNotNull(
//        savedStateHandle[AlbumDestination.albumIdArg]
//    )

    private val albumIdTrigger: MutableSharedFlow<Long> = MutableSharedFlow(replay = 1)

    fun setAlbumId(id: Long) = viewModelScope.launch {
        albumIdTrigger.emit(id)
    }

    val album: StateFlow<Resource<AlbumUi>> = albumIdTrigger.flatMapLatest {
        getAlbumFlow(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResourceLoading()
    )

    private fun getAlbumFlow(id: Long?): Flow<Resource<AlbumUi>> =
        if (id == null) {
            flow { emit(ResourceLoading()) }
        } else {
            albumUseCase.getAlbumById(id)
                .map {
                    when (it) {
                        is ResultSuccess -> ResourceSuccess(it.data.toUi())
                        is ResultError -> {
                            Timber.e("Failed to getAlbum")
                            ResourceError(
                                errorMessage = ErrorMessage.ErrorMessageString(
                                    it.message
                                )
                            )
                        }
                        is ResultException -> {
                            Timber.e(it.throwable, "Error when getAlbum")
                            ResourceError(
                                errorMessage = ErrorMessage.ErrorMessageResource(
                                    R.string.get_album_exception
                                )
                            )
                        }
                    }
                }.onStart {
                    emit(ResourceLoading())
                }
        }
}
