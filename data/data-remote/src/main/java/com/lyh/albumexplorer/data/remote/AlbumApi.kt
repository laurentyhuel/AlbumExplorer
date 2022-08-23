package com.lyh.albumexplorer.data.remote

import com.lyh.albumexplorer.data.remote.dto.AlbumDto
import retrofit2.Response
import retrofit2.http.GET

interface AlbumApi {

    @GET("6047a7dc-9550-477b-90f4-26cee644c4ab")
    suspend fun getAlbums(): Response<List<AlbumDto>>
}

