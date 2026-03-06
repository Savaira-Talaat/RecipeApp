package com.example.recipeapp.viewmodel

import androidx.compose.runtime.getValue
import com.example.recipeapp.util.Result
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.repository.MealRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Meal
import com.example.recipeapp.model.MealDetail
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {
    private val repository = MealRepository()

    var mealsState by mutableStateOf<Result<List<Meal>>>(Result.Loading)
        private set

    var categoriesState by mutableStateOf<Result<List<Category>>>(Result.Loading)
        private set

    var detailState by mutableStateOf<Result<MealDetail>?>(null)
        private set

    fun searchMeals(query: String = "") {
        mealsState = Result.Loading
        viewModelScope.launch {
            mealsState = repository.searchMeals(query)
        }
    }

    fun loadMealDetail(id: String) {
        detailState = Result.Loading
        viewModelScope.launch {
            detailState = repository.getMealDetail(id)
        }
    }

    fun loadMealsByCategory(category: String) {
        mealsState = Result.Loading
        viewModelScope.launch {
            mealsState = repository.getMealsByCategory(category)
        }
    }
}