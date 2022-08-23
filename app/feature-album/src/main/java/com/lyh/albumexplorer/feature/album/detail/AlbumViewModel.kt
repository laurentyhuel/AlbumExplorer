package com.lyh.albumexplorer.feature.album.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lyh.albumexplorer.domain.AlbumUseCase
import com.lyh.albumexplorer.domain.core.ResultError
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.feature.album.mapper.toUi
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class AlbumViewModel(
    private val albumUseCase: AlbumUseCase
) : ViewModel() {

    private val albumIdTrigger: MutableSharedFlow<Long> = MutableSharedFlow(replay = 1)

    fun setAlbumId(id: Long) = viewModelScope.launch {
        albumIdTrigger.emit(id)
    }

    val album: LiveData<Resource<AlbumUi>> = albumIdTrigger
        .flatMapLatest { id ->
            albumUseCase.getAlbumById(id)
        }.map {
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
        }.asLiveData(viewModelScope.coroutineContext)
}
