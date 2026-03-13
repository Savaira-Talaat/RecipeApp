package com.example.recipeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Représente une recette stockée en local
@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String?,
    val area: String?,
    val imageUrl: String?,
    // Timestamp pour savoir quand la donnée a été mise en cache
    val cachedAt: Long = System.currentTimeMillis()
)