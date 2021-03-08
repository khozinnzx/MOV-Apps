package com.example.mov_apps.api

import com.example.mov_apps.model.Movie
import com.example.mov_apps.utils.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("/3/movie/now_playing?api_key=${API_KEY}&language=en-US")
    suspend fun getMoviesNowPlaying(
        @Query("page")
        pageNumber: Int = 1
    ): Response<Movie>

    @GET("/3/movie/upcoming?api_key=${API_KEY}&language=en-US")
    suspend fun getMovieComingSoon(
        @Query("page")
        pageNumber: Int = 1
    ): Response<Movie>
}