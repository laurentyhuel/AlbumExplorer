package com.lyh.albumexplorer.feature.album

import android.content.Context
import app.cash.turbine.test
import com.lyh.albumexplorer.domain.AlbumUseCase
import com.lyh.albumexplorer.domain.core.ResultError
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.feature.album.list.AlbumListViewModel
import com.lyh.albumexplorer.feature.album.util.CoroutinesTestExtension
import com.lyh.albumexplorer.feature.album.util.InstantExecutorExtension
import com.lyh.albumexplorer.feature.core.ResourceError
import com.lyh.albumexplorer.feature.core.ResourceLoading
import com.lyh.albumexplorer.feature.core.ResourceSuccess
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeoutException

@ExtendWith(
    InstantExecutorExtension::class,
    CoroutinesTestExtension::class
)
class AlbumListViewModelTest {

    private val albums = List(10) { index -> createAlbumModel(index.toLong()) }

    private val context = mockk<Context>()
    private val albumUseCase = mockk<AlbumUseCase>()

    private lateinit var albumListViewModel: AlbumListViewModel

    @BeforeEach
    fun initVM() {
        albumListViewModel = AlbumListViewModel(albumUseCase)
    }

    @Test
    fun `WHEN get albums return exception THEN collecting flow get error`() = runTest {

        coEvery { albumUseCase.getAlbums() } returns flow {
            emit(ResultException(TimeoutException()))
        }
        every { context.getString(any()) } returns "error occurred"

        albumListViewModel.albums.test {
            val resultLoading = awaitItem()
            assertTrue(resultLoading is ResourceLoading)

            val result = awaitItem()
            assertTrue(result is ResourceError)
            val error = result as ResourceError
            assertEquals("error occurred", error.errorMessage.getMessage(context))
        }
    }

    @Test
    fun `WHEN get albums return error THEN collecting flow get error`() = runTest {
        coEvery { albumUseCase.getAlbums() } returns flow {
            emit(ResultError(400, "Bad request"))
        }

        albumListViewModel.albums.test {
            val resultLoading = awaitItem()
            assertTrue(resultLoading is ResourceLoading)

            val result = awaitItem()
            assertTrue(result is ResourceError)
            val error = result as ResourceError
            assertEquals("Bad request", error.errorMessage.getMessage(context))
        }

    }

    @Test
    fun `WHEN get albums return data THEN collecting flow get data`() = runTest {
        coEvery { albumUseCase.getAlbums() } returns flow {
            emit(ResultSuccess(albums))
        }

        albumListViewModel.albums.test {
            val resultLoading = awaitItem()
            // Verify that the first value is Loading
            assertTrue(resultLoading is ResourceLoading)

            val resultSuccess = awaitItem()
            assertTrue(resultSuccess is ResourceSuccess)
            val success = resultSuccess as ResourceSuccess
            assertEquals(albums.size, success.data.size)
        }
    }

    @Test
    fun `WHEN get albums after an error retry and get data THEN collecting flow get data`() = runTest {
        coEvery { albumUseCase.getAlbums() } returns flow {
            emit(ResultException(TimeoutException()))
        }
        every { context.getString(any()) } returns "error occurred"

        albumListViewModel.albums.test {


            val resultLoadingError = awaitItem()
            assertTrue(resultLoadingError is ResourceLoading)

            val resultError = awaitItem()
            assertTrue(resultError is ResourceError)
            val error = resultError as ResourceError
            assertEquals("error occurred", error.errorMessage.getMessage(context))

            coEvery { albumUseCase.getAlbums() } returns flow {
                emit(ResultSuccess(albums))
            }

            albumListViewModel.triggerAlbums()


            val resultLoadingSuccess = awaitItem()
            // Verify that the first value is Loading
            assertTrue(resultLoadingSuccess is ResourceLoading)

            val resultSuccess = awaitItem()
            assertTrue(resultSuccess is ResourceSuccess)
            val success = resultSuccess as ResourceSuccess
            assertEquals(albums.size, success.data.size)
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
