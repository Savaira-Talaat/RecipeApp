package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.recipeapp.activity.RecipeAppActivity
import com.example.recipeapp.data.local.AppDatabase
import com.example.recipeapp.data.repository.MealRepository
import com.example.recipeapp.ui.screen.SplashScreen
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val repository = MealRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // modifLocal : initialise la BDD au démarrage
        AppDatabase.init(application)

        setContent {
            RecipeAppTheme {
                SplashScreen()
            }
        }

        lifecycleScope.launch {
            val preloadJob = launch {
                repository.searchMeals("")
            }

            delay(1500)
            preloadJob.join()

            startActivity(Intent(this@MainActivity, RecipeAppActivity::class.java))
            finish()
        }
    }
}