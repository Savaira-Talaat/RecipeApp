package com.example.recipeapp.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idMeal")
    val id: String,
    @SerializedName("strMeal")
    val title: String,
    @SerializedName("strCategory")
    val category: String?,
    @SerializedName("strArea")
    val area: String?,
    @SerializedName("strMealThumb")
    val imageUrl: String?
)