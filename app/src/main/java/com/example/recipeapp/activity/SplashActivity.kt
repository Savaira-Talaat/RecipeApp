package com.example.recipeapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.recipeapp.MainActivity
import com.example.recipeapp.data.repository.MealRepository
import com.example.recipeapp.ui.screen.SplashScreen
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    private val repository = MealRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent { RecipeAppTheme {
            SplashScreen()
            }
        }

        lifecycleScope.launch {
            val preloadJob = launch {
                repository.searchMeals("")
            }

            delay(1500)
            preloadJob.join()
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}