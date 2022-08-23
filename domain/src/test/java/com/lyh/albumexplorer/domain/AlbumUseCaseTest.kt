package com.lyh.albumexplorer.domain

import app.cash.turbine.test
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.domain.model.AlbumModel
import com.lyh.albumexplorer.domain.repository.IAlbumRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AlbumUseCaseTest {

    private val albums = List(10) { index -> createAlbumModel(index.toLong()) }

    private val albumRepository = mockk<IAlbumRepository>(relaxed = true)
    private val albumUseCase = AlbumUseCase(albumRepository)

    @Test
    fun `WHEN get new albums THEN return albums`() = runTest {

        coEvery { albumUseCase.getAlbums() } returns flow { emit(ResultSuccess(albums)) }
        albumUseCase.getAlbums()
            .test {
                val result = awaitItem()

                assertTrue(result is ResultSuccess<*>)
                val resultSuccess = result as ResultSuccess<List<AlbumModel>>

                assertEquals(albums.size, resultSuccess.data.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get new album by id THEN return expected album`() = runTest {

        val album = createAlbumModel(5L)

        coEvery { albumUseCase.getAlbumById(album.id) } returns flow { emit(ResultSuccess(album)) }
        albumUseCase.getAlbumById(album.id)
            .test {
                val result = awaitItem()

                assertTrue(result is ResultSuccess)
                val resultSuccess = result as ResultSuccess

                assertEquals(album.id, resultSuccess.data.id)
                assertEquals(album.title, resultSuccess.data.title)

                awaitComplete()
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
