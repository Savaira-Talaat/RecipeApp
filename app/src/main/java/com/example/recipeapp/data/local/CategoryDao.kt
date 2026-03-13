package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {

    // Récupère toutes les catégories en cache
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    // Insère ou remplace les catégories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    // Récupère le timestamp du dernier cache
    @Query("SELECT cachedAt FROM categories ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getLastCachedAt(): Long?
}