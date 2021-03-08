package com.example.mov_apps.repository

import com.example.mov_apps.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesRepository {

    suspend fun getMoviesNowPlaying(pageNumber: Int) =
        RetrofitInstance.api.getMoviesNowPlaying(pageNumber)

    suspend fun getComingSoonMovie(pageNumber: Int) =
        RetrofitInstance.api.getMovieComingSoon(pageNumber)

}