package com.lyh.albumexplorer.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.lyh.albumexplorer.data.local.dao.AlbumDao
import com.lyh.albumexplorer.data.local.entity.AlbumEntity
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AlbumDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var albumDao: AlbumDao
    private val albums = List(10) { index -> createAlbum(index.toLong()) }

    @BeforeEach
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        albumDao = database.albumDao()
    }

    @AfterEach
    fun closeDb() = database.close()

    @Test
    fun testInsertData() = runBlocking {
        val albumDao = database.albumDao()

        albumDao.insertOrUpdateAlbums(albums)

        val albumsInserted = albumDao.getAlbums()
        assertEquals(albums.size, albumsInserted.size)
        // Album is a data class, so check is made on properties, and not instance :)
        assertEquals(albums.first { it.id == 5L }, albumsInserted.first { it.id == 5L })
    }

    @Test
    fun testGetAlbumById() = runBlocking {
        val albumDao = database.albumDao()

        albumDao.insertOrUpdateAlbums(albums)

        val album = albumDao.getAlbumById(5L)
        assertEquals(5L, album.id)
        assertEquals("Title 5", album.title)
    }

    @Test
    fun testDeleteData() = runBlocking {
        val albumDao = database.albumDao()

        // start by inserting data
        albumDao.insertOrUpdateAlbums(albums)

        val albumsInserted = albumDao.getAlbums()
        assertEquals(albums.size, albumsInserted.size)

        // delete an album
        val albumToDelete = createAlbum(1)
        albumDao.deleteAlbums(listOf(albumToDelete))

        val albumsAfterDelete = albumDao.getAlbums()
        assertEquals(albums.size - 1, albumsAfterDelete.size)
    }

    @Test
    fun testUpdateData() = runBlocking {
        val albumDao = database.albumDao()

        // start by inserting data
        albumDao.insertOrUpdateAlbums(albums)

        val albumsInserted = albumDao.getAlbums()
        assertEquals(albums.size, albumsInserted.size)
        assertEquals("Title 5", albumsInserted.first { it.id == 5L }.title)

        // update an album
        val albumToUpdate = createAlbum(5).apply { title = "title updated" }
        albumDao.insertOrUpdateAlbums(listOf(albumToUpdate))

        val albumsAfterUpdate = albumDao.getAlbums()
        assertEquals("title updated", albumsAfterUpdate.first { it.id == 5L }.title)
    }

    private fun createAlbum(id: Long) = AlbumEntity(
        1,
        id,
        "Title $id",
        "https://myurl.com/$id.jpg",
        "https://myurl.com/tiny/$id.jpg"
    )
}

