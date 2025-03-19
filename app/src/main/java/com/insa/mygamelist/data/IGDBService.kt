package com.insa.mygamelist.data

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IGDBApiService {

    @POST("games")
    suspend fun getGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody,
    ): List<Game>

    @POST("covers")
    suspend fun getCovers(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody,
    ): List<Cover>

    @POST("genres")
    suspend fun getGenres(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody,
    ): List<Genre>

    @POST("platforms")
    suspend fun getPlatformss(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") authHeader: String,
        @Body body: RequestBody,
    ): List<Platform>
}

//pour utiliser ce fichier, il faudrait mettre les déclarations de dataclass de IGDB.kt en @Serializable