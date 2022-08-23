package com.lyh.albumexplorer.feature.album

import com.lyh.albumexplorer.domain.AlbumUseCase
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.feature.album.detail.AlbumViewModel
import com.lyh.albumexplorer.feature.album.util.CoroutinesTestExtension
import com.lyh.albumexplorer.feature.album.util.InstantExecutorExtension
import com.lyh.albumexplorer.feature.album.util.getOrAwaitValue
import com.lyh.albumexplorer.feature.core.ResourceError
import com.lyh.albumexplorer.feature.core.ResourceLoading
import com.lyh.albumexplorer.feature.core.ResourceSuccess
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeoutException

@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class AlbumViewModelTest {

    private val albumUseCase = mockk<AlbumUseCase>()
    private lateinit var albumViewModel: AlbumViewModel

    @BeforeEach
    fun initVM() {
        albumViewModel = AlbumViewModel(albumUseCase)
    }

    @Test
    fun `WHEN get album by id THEN observing livedata get data`() = runTest {

        val album = createAlbumModel(5L)

        coEvery { albumUseCase.getAlbumById(5L) } returns flow { emit(ResultSuccess(album)) }

        albumViewModel.setAlbumId(5L)

        val resultLoading = albumViewModel.album.getOrAwaitValue()
        assertTrue(resultLoading is ResourceLoading)

        val result = albumViewModel.album.getOrAwaitValue()
        assertTrue(result is ResourceSuccess)
        val albumResult = result as ResourceSuccess
        assertEquals(5L, albumResult.data.id)
        assertEquals("Title 5", albumResult.data.title)
    }

    @Test
    fun `WHEN get album by id returns exception THEN observing livedata get exception`() = runTest {


        coEvery { albumUseCase.getAlbumById(5L) } returns flow {
            emit(
                ResultException(
                    TimeoutException()
                )
            )
        }

        albumViewModel.setAlbumId(5L)

        val resultLoading = albumViewModel.album.getOrAwaitValue()
        assertTrue(resultLoading is ResourceLoading)

        val result = albumViewModel.album.getOrAwaitValue()
        assertTrue(result is ResourceError)
    }

    private fun createAlbumModel(id: Long) = AlbumModel(
        1,
        id,
        "Title $id",
        "https://myurl.com/$id.jpg",
        "https://myurl.com/tiny/$id.jpg"
    )
}
