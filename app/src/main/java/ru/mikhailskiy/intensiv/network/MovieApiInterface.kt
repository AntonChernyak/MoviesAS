package ru.mikhailskiy.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.mikhailskiy.intensiv.data.credits_model.CreditsResponse
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsDTO
import ru.mikhailskiy.intensiv.data.movie_model.MovieResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>

    @GET("tv/popular")
    fun getPopularTvShowsList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru"
    ): Call<MovieDetailsDTO>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru"
    ): Call<CreditsResponse>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendationsMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    /*@GET("/authentication/token/new")
    fun getAuthenticationToken(@Query("api_key") apiKey: String) : Call<AuthenticationResponse>*/
}