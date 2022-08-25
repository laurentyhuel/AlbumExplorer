package com.lyh.albumexplorer.feature.album

import app.cash.turbine.test
import com.lyh.albumexplorer.domain.AlbumUseCase
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.feature.album.detail.AlbumViewModel
import com.lyh.albumexplorer.feature.album.util.CoroutinesTestExtension
import com.lyh.albumexplorer.feature.album.util.InstantExecutorExtension
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
    fun `WHEN get album by id THEN collecting flow get data`() = runTest {

        val albumId = 5L
        val album = createAlbumModel(albumId)

        coEvery { albumUseCase.getAlbumById(albumId) } returns flow { emit(ResultSuccess(album)) }

        albumViewModel.setAlbumId(albumId)

        albumViewModel.album.test {
            val resultLoading = awaitItem()
            assertTrue(resultLoading is ResourceLoading)

            val result = awaitItem()
            assertTrue(result is ResourceSuccess)
            val albumResult = result as ResourceSuccess
            assertEquals(albumId, albumResult.data.id)
            assertEquals(album.title, albumResult.data.title)
        }
    }

    @Test
    fun `WHEN get album by id returns exception THEN collecting flow get exception`() = runTest {

        val albumId = 5L
        coEvery { albumUseCase.getAlbumById(albumId) } returns flow {
            emit(
                ResultException(
                    TimeoutException()
                )
            )
        }

        albumViewModel.setAlbumId(albumId)

        albumViewModel.album.test {
            val resultLoading = awaitItem()
            assertTrue(resultLoading is ResourceLoading)

            val result = awaitItem()
            assertTrue(result is ResourceError)
        }
    }

    private fun createAlbumModel(id: Long) = AlbumModel(
        1,
        id,
        "Title $id",
        "https://myurl.com/$id.jpg",
        "https://myurl.com/tiny/$id.jpg"
    )
}
