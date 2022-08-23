package com.lyh.albumexplorer.data.local.di

import android.content.Context
import androidx.room.Room
import com.lyh.albumexplorer.data.local.AppDatabase
import org.koin.dsl.module

/**
 * Koin module for data-local
 */
val dataLocalModule = module {
    single { providesAppDatabase(get()) }
    single { providesAlbumDao(get()) }
}

fun providesAlbumDao(appDatabase: AppDatabase) = appDatabase.albumDao()

fun providesAppDatabase(
    context: Context,
): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    DATABASE_NAME
).build()

private const val DATABASE_NAME = "album-database"