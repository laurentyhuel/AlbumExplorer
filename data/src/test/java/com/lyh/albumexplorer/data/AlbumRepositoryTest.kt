package com.lyh.albumexplorer.data

import app.cash.turbine.test
import com.lyh.albumexplorer.data.core.AppDispatchers
import com.lyh.albumexplorer.data.local.dao.AlbumDao
import com.lyh.albumexplorer.data.local.entity.AlbumEntity
import com.lyh.albumexplorer.data.remote.AlbumApi
import com.lyh.albumexplorer.data.remote.dto.AlbumDto
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import com.lyh.albumexplorer.domain.model.AlbumModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.util.concurrent.TimeoutException

class AlbumRepositoryTest {

    private val albumsFromRemote = List(10) { index -> createAlbumDto(index.toLong()) }
    private val albumsFromLocal = List(15) { index -> createAlbumEntity(index.toLong()) }
    private val albumsFromLocalSame = List(10) { index -> createAlbumEntity(index.toLong()) }

    private val albumApi = mockk<AlbumApi>()
    private val albumDao = mockk<AlbumDao>(relaxed = true)
    private val dispatchers = AppDispatchers(UnconfinedTestDispatcher(), UnconfinedTestDispatcher())
    private val albumRepository = AlbumRepository(albumApi, albumDao, dispatchers)

    @Test
    fun `WHEN get new albums from remote THEN fetch, sync locally and return albums`() = runTest {

        coEvery { albumApi.getAlbums() } returns Response.success(albumsFromRemote)
        coEvery { albumDao.getAlbums() } returns albumsFromLocal
        albumRepository.getAlbums()
            .test {
                val result = awaitItem()

                assertTrue(result is ResultSuccess<*>)
                val resultSuccess = result as ResultSuccess<List<AlbumModel>>

                assertEquals(albumsFromRemote.size, resultSuccess.data.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get same albums from remote THEN fetch, sync locally(no effect) and return albums`() =
        runTest {

            coEvery { albumApi.getAlbums() } returns Response.success(albumsFromRemote)
            coEvery { albumDao.getAlbums() } returns albumsFromLocalSame
            albumRepository.getAlbums()
                .test {
                    val result = awaitItem()

                    assertTrue(result is ResultSuccess<*>)
                    val resultSuccess = result as ResultSuccess<List<AlbumModel>>

                    assertEquals(albumsFromRemote.size, resultSuccess.data.size)

                    awaitComplete()
                }
        }

    @Test
    fun `WHEN get album by id THEN return expected album`() = runTest {
        val album = createAlbumEntity(5L)

        coEvery { albumDao.getAlbumById(album.id) } returns album

        albumRepository.getAlbumById(album.id)
            .test {
                val result = awaitItem()

                assertTrue(result is ResultSuccess)
                val resultSuccess = result as ResultSuccess

                assertEquals(album.id, resultSuccess.data.id)
                assertEquals(album.title, result.data.title)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get album by id return exception THEN return ResultException`() = runTest {

        coEvery { albumDao.getAlbumById(5L) } throws TimeoutException()

        albumRepository.getAlbumById(5L).test {
            val result = awaitItem()

            assertTrue(result is ResultException)

            awaitComplete()
        }
    }

    private fun createAlbumDto(id: Long) = AlbumDto(
        1,
        id,
        "Title $id",
        "https://myurl.com/$id.jpg",
        "https://myurl.com/tiny/$id.jpg"
    )

    private fun createAlbumEntity(id: Long) = AlbumEntity(
        1,
        id,
        "Title $id",
        "https://myurl.com/$id.jpg",
        "https://myurl.com/tiny/$id.jpg"
    )
}
