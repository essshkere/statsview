package ru.netology.statsview.api

import ru.netology.statsview.data.Album
import retrofit2.http.GET


interface AlbumService {
    @GET("album.json")
    suspend fun getAlbum(): Album
}