package com.lyh.albumexplorer.data.local.dao

import androidx.room.*
import com.lyh.albumexplorer.data.local.entity.AlbumEntity

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAlbums(albums: List<AlbumEntity>)

    @Query(value = "SELECT * FROM ${AlbumEntity.TABLE_NAME}")
    suspend fun getAlbums(): List<AlbumEntity>

    @Delete
    suspend fun deleteAlbums(albums: List<AlbumEntity>)

    @Query(value = "SELECT * FROM ${AlbumEntity.TABLE_NAME} WHERE id = :id")
    suspend fun getAlbumById(id: Long): AlbumEntity
}