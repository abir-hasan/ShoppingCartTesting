package net.omobio.shoppingcarttesting.data.remote

import net.omobio.shoppingcarttesting.BuildConfig
import net.omobio.shoppingcarttesting.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api")
    suspend fun searchForImage(
        @Query("q") searchString: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>

}