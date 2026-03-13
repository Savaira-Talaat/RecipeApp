package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    // Récupère toutes les recettes en cache
    @Query("SELECT * FROM meals")
    suspend fun getAllMeals(): List<MealEntity>

    // Récupère les recettes filtrées par catégorie
    @Query("SELECT * FROM meals WHERE category = :category")
    suspend fun getMealsByCategory(category: String): List<MealEntity>

    // Récupère les recettes dont le titre contient la recherche
    @Query("SELECT * FROM meals WHERE title LIKE '%' || :query || '%'")
    suspend fun searchMeals(query: String): List<MealEntity>

    // Insère ou remplace une recette
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    // Supprime toutes les recettes
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()

    // Récupère le timestamp du dernier cache
    @Query("SELECT cachedAt FROM meals ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getLastCachedAt(): Long?
}