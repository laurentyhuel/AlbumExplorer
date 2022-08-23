package com.lyh.albumexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lyh.albumexplorer.data.local.dao.AlbumDao
import com.lyh.albumexplorer.data.local.entity.AlbumEntity

@Database(entities = [AlbumEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}