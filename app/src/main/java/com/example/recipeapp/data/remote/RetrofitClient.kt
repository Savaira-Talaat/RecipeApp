package com.example.recipeapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val apiService: MealApiService by lazy { // by lazy ça permet de dire que la connexion n'est créée que la première fois que tu en as besoin pas au démarrage de l'appli
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }
}