package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDetailDao {

    @Query("SELECT * FROM meal_details WHERE id = :id")
    suspend fun getMealDetailById(id: String): MealDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealDetail(detail: MealDetailEntity)
}