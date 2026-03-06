package com.example.recipeapp.data.remote

import retrofit2.http.Query
import retrofit2.http.GET

interface MealApiService {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: String
    ) : MealDetailResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryListResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ) : MealListResponse
}