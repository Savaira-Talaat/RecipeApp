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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {
    private val repository = MealRepository()

    private var currentMealsList = mutableListOf<Meal>()

    var mealsState by mutableStateOf<Result<List<Meal>>>(Result.Loading)
        private set

    var categoriesState by mutableStateOf<Result<List<Category>>>(Result.Loading)
        private set

    var detailState by mutableStateOf<Result<MealDetail>?>(null)
        private set

    var isLoadingMore by mutableStateOf(false)
        private set

    // Catégorie active (null = aucun filtre)
    var selectedCategory by mutableStateOf<String?>(null)
        private set

    // Charge les catégories au démarrage
    init {
        viewModelScope.launch {
            categoriesState = repository.getCategories()
        }
    }

    // Sélectionne ou désélectionne une catégorie
    fun toggleCategoryFilter(category: String) {
        if (selectedCategory == category) {
            selectedCategory = null
            loadRandomMeals()
        } else {
            selectedCategory = category
            loadMealsByCategory(category)
        }
    }

    fun searchMeals(query: String = "") {
        // Recherche texte = reset filtre catégorie
        selectedCategory = null
        mealsState = Result.Loading
        viewModelScope.launch {
            delay(500)
            val result = repository.searchMeals(query)
            if (result is Result.Success) {
                currentMealsList = result.data.toMutableList()
            }
            mealsState = result
        }
    }

    fun loadRandomMeals(count: Int = 10, clear: Boolean = true) {
        if (clear) {
            mealsState = Result.Loading
            currentMealsList.clear()
        } else {
            // Bloque le scroll infini si filtre actif
            if (isLoadingMore || selectedCategory != null) return
            isLoadingMore = true
        }

        viewModelScope.launch {
            delay(500)
            val deferredMeals = (1..count).map {
                async { repository.getRandomMeal() }
            }

            val results = deferredMeals.awaitAll()
            val newMeals = results.filterIsInstance<Result.Success<Meal>>().map { it.data }

            if (newMeals.isNotEmpty()) {
                val existingIds = currentMealsList.map { it.id }.toSet()
                val uniqueNewMeals = newMeals.filter { it.id !in existingIds }
                currentMealsList.addAll(uniqueNewMeals)
                mealsState = Result.Success(currentMealsList.toList())
            } else if (currentMealsList.isEmpty()) {
                mealsState = Result.Error("Erreur lors du chargement des recettes.")
            }
            isLoadingMore = false
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
        // Vide la liste avant nouveau filtre
        currentMealsList.clear()
        viewModelScope.launch {
            mealsState = repository.getMealsByCategory(category)
        }
    }
}