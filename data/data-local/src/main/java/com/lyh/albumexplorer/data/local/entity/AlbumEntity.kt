package com.lyh.albumexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lyh.albumexplorer.data.local.entity.AlbumEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class AlbumEntity(
    var albumId: Long,
    @PrimaryKey
    var id: Long,
    var title: String,
    var url: String,
    var thumbnailUrl: String,
) {
    companion object {
        const val TABLE_NAME = "album"
    }
}

