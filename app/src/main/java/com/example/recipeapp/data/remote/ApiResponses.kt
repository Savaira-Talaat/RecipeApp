package com.example.recipeapp.data.remote

import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Meal
import com.example.recipeapp.model.MealDetail
import com.google.gson.annotations.SerializedName

data class MealListResponse(
    @SerializedName("meals")
    val meals: List<Meal>?
)

data class MealDetailResponse(
    @SerializedName("meals")
    val meals: List<MealDetail>?
)

data class CategoryListResponse(
    @SerializedName("categories")
    val categories: List<Category>?
)