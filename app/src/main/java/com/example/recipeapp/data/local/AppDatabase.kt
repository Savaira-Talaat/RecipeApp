package com.example.recipeapp.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MealEntity::class, CategoryEntity::class, MealDetailEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao
    abstract fun categoryDao(): CategoryDao
    abstract fun mealDetailDao(): MealDetailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun init(app: Application) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        app,
                        AppDatabase::class.java,
                        "recipe_app_database"
                    )
                        // modifLocal : migration auto si version change
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }

        fun get(): AppDatabase {
            return INSTANCE ?: error("AppDatabase non initialisée.")
        }
    }
}