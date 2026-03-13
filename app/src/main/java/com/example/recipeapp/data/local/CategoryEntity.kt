package com.example.recipeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Représente une catégorie stockée en local
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String?,
    // Timestamp pour savoir quand la donnée a été mise en cache
    val cachedAt: Long = System.currentTimeMillis()
)