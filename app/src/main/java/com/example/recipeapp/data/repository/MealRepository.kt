package com.example.recipeapp.data.repository

import com.example.recipeapp.data.remote.RetrofitClient
import com.example.recipeapp.util.Result
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Meal
import com.example.recipeapp.model.MealDetail

class MealRepository {
    private val api = RetrofitClient.apiService

    suspend fun searchMeals(query: String): Result<List<Meal>> {
        return try {
            val response = api.searchMeals(query)
            Result.Success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.Error("Pas de connexion. Vérifiez votre réseau")
        }
    }
    suspend fun getMealDetail(id: String): Result<MealDetail> {
        return try {
            val detail = api.getMealById(id).meals?.firstOrNull()
                ?: return Result.Error("Recette introuvable.")
            Result.Success(detail)
        } catch (e: Exception) {
            Result.Error("Pas de connexion. Vérifiez votre réseau.")
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            Result.Success(response.categories ?: emptyList())
        } catch (e: Exception) {
            Result.Error("Pas de connexion. Vérifiez votre réseau")
        }
    }

    suspend fun getMealsByCategory(category: String): Result<List<Meal>> {
        return try {
            val response = api.getMealsByCategory(category)
            Result.Success(response.meals ?: emptyList())
        } catch (e: Exception) {
            Result.Error("Pas de connexion. Vérifez votre réeau")
        }
    }

    suspend fun getRandomMeal(): Result<Meal> {
        return try {
            val response = api.getRandomMeal()
            val meal = response.meals?.firstOrNull() ?: return Result.Error("Aucune recette trouvée.")
            Result.Success(meal)
        } catch (e: Exception) {
            Result.Error("Erreur lors de la récupération d'une recette aléatoire.")
        }
    }
}
