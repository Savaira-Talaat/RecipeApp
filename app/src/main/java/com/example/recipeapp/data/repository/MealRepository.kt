package com.example.recipeapp.data.repository

import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.local.CategoryEntity
import com.example.recipeapp.data.local.MealDetailEntity
import com.example.recipeapp.data.local.MealEntity
import com.example.recipeapp.data.remote.RetrofitClient
import com.example.recipeapp.util.Result
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Meal
import com.example.recipeapp.model.MealDetail

class MealRepository {
    private val api = RetrofitClient.apiService

    // modifLocal : accès aux DAOs de la BDD locale
    private val mealDao by lazy { AppDatabase.get().mealDao() }
    private val categoryDao by lazy { AppDatabase.get().categoryDao() }
    private val mealDetailDao by lazy { AppDatabase.get().mealDetailDao() }

    // modifLocal : durée de validité du cache
    private val cacheExpiration = 60 * 60 * 1000L

    // modifLocal : vérifie si le cache est encore valide
    private fun isCacheValid(cachedAt: Long?): Boolean {
        if (cachedAt == null) return false
        return System.currentTimeMillis() - cachedAt < cacheExpiration
    }

    // modifLocal : conversions entre modèles réseau et BDD
    private fun MealEntity.toMeal() = Meal(id, title, category, area, imageUrl)
    private fun Meal.toEntity() = MealEntity(id, title, category, area, imageUrl)
    private fun CategoryEntity.toCategory() = Category(id, name, imageUrl)
    private fun Category.toEntity() = CategoryEntity(id, name, imageUrl)

    // modifLocal : convertit MealDetail en MealDetailEntity
    private fun MealDetail.toEntity() = MealDetailEntity(
        id = id, title = title, imageUrl = imageUrl,
        category = category, area = area, instructions = instructions,
        ingredient1 = ingredient1, measure1 = measure1,
        ingredient2 = ingredient2, measure2 = measure2,
        ingredient3 = ingredient3, measure3 = measure3,
        ingredient4 = ingredient4, measure4 = measure4,
        ingredient5 = ingredient5, measure5 = measure5,
        ingredient6 = ingredient6, measure6 = measure6,
        ingredient7 = ingredient7, measure7 = measure7,
        ingredient8 = ingredient8, measure8 = measure8,
        ingredient9 = ingredient9, measure9 = measure9,
        ingredient10 = ingredient10, measure10 = measure10,
        ingredient11 = ingredient11, measure11 = measure11,
        ingredient12 = ingredient12, measure12 = measure12,
        ingredient13 = ingredient13, measure13 = measure13,
        ingredient14 = ingredient14, measure14 = measure14,
        ingredient15 = ingredient15, measure15 = measure15,
        ingredient16 = ingredient16, measure16 = measure16,
        ingredient17 = ingredient17, measure17 = measure17,
        ingredient18 = ingredient18, measure18 = measure18,
        ingredient19 = ingredient19, measure19 = measure19,
        ingredient20 = ingredient20, measure20 = measure20
    )

    // modifLocal : convertit MealDetailEntity en MealDetail
    private fun MealDetailEntity.toMealDetail() = MealDetail(
        id = id, title = title, imageUrl = imageUrl,
        category = category, area = area, instructions = instructions,
        ingredient1 = ingredient1, measure1 = measure1,
        ingredient2 = ingredient2, measure2 = measure2,
        ingredient3 = ingredient3, measure3 = measure3,
        ingredient4 = ingredient4, measure4 = measure4,
        ingredient5 = ingredient5, measure5 = measure5,
        ingredient6 = ingredient6, measure6 = measure6,
        ingredient7 = ingredient7, measure7 = measure7,
        ingredient8 = ingredient8, measure8 = measure8,
        ingredient9 = ingredient9, measure9 = measure9,
        ingredient10 = ingredient10, measure10 = measure10,
        ingredient11 = ingredient11, measure11 = measure11,
        ingredient12 = ingredient12, measure12 = measure12,
        ingredient13 = ingredient13, measure13 = measure13,
        ingredient14 = ingredient14, measure14 = measure14,
        ingredient15 = ingredient15, measure15 = measure15,
        ingredient16 = ingredient16, measure16 = measure16,
        ingredient17 = ingredient17, measure17 = measure17,
        ingredient18 = ingredient18, measure18 = measure18,
        ingredient19 = ingredient19, measure19 = measure19,
        ingredient20 = ingredient20, measure20 = measure20
    )

    suspend fun searchMeals(query: String): Result<List<Meal>> {
        return try {
            val meals = api.searchMeals(query).meals ?: emptyList()
            // modifLocal : sauvegarde recettes après chargement réseau
            mealDao.insertMeals(meals.map { it.toEntity() })
            Result.Success(meals)
        } catch (e: Exception) {
            // modifLocal : fallback sur le cache si pas de réseau
            val cached = if (query.isEmpty()) mealDao.getAllMeals() else mealDao.searchMeals(query)
            if (cached.isNotEmpty()) Result.Success(cached.map { it.toMeal() })
            else Result.Error("Pas de connexion. Vérifiez votre réseau")
        }
    }

    suspend fun getMealDetail(id: String): Result<MealDetail> {
        return try {
            val detail = api.getMealById(id).meals?.firstOrNull()
                ?: return Result.Error("Recette introuvable.")
            // modifLocal : sauvegarde le détail en cache
            mealDetailDao.insertMealDetail(detail.toEntity())
            Result.Success(detail)
        } catch (e: Exception) {
            // modifLocal : fallback cache si pas de réseau
            val cached = mealDetailDao.getMealDetailById(id)
            if (cached != null) Result.Success(cached.toMealDetail())
            else Result.Error("Pas de connexion. Vérifiez votre réseau.")
        }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val categories = api.getCategories().categories ?: emptyList()
            // modifLocal : sauvegarde catégories après chargement réseau
            categoryDao.insertCategories(categories.map { it.toEntity() })
            Result.Success(categories)
        } catch (e: Exception) {
            // modifLocal : fallback sur le cache si pas de réseau
            val cached = categoryDao.getAllCategories()
            if (cached.isNotEmpty()) Result.Success(cached.map { it.toCategory() })
            else Result.Error("Pas de connexion. Vérifiez votre réseau")
        }
    }

    suspend fun getMealsByCategory(category: String): Result<List<Meal>> {
        return try {
            val meals = api.getMealsByCategory(category).meals ?: emptyList()
            // modifLocal : sauvegarde recettes filtrées en cache
            mealDao.insertMeals(meals.map { it.toEntity() })
            Result.Success(meals)
        } catch (e: Exception) {
            // modifLocal : fallback sur le cache si pas de réseau
            val cached = mealDao.getMealsByCategory(category)
            if (cached.isNotEmpty()) Result.Success(cached.map { it.toMeal() })
            else Result.Error("Pas de connexion. Vérifiez votre réseau")
        }
    }

    suspend fun getRandomMeal(): Result<Meal> {
        return try {
            val meal = api.getRandomMeal().meals?.firstOrNull()
                ?: return Result.Error("Aucune recette trouvée.")
            // modifLocal : sauvegarde recette aléatoire en cache
            mealDao.insertMeals(listOf(meal.toEntity()))
            Result.Success(meal)
        } catch (e: Exception) {
            Result.Error("Erreur lors de la récupération d'une recette aléatoire.")
        }
    }

    // modifLocal : vérifie fraîcheur du cache recettes
    suspend fun isMealCacheValid(): Boolean = isCacheValid(mealDao.getLastCachedAt())
    // modifLocal : vérifie fraîcheur du cache catégories
    suspend fun isCategoryCacheValid(): Boolean = isCacheValid(categoryDao.getLastCachedAt())
    // modifLocal : retourne toutes les recettes du cache
    suspend fun getCachedMeals(): List<Meal> = mealDao.getAllMeals().map { it.toMeal() }
}