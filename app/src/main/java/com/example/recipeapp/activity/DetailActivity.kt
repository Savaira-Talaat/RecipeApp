package com.example.recipeapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recipeapp.ui.screen.DetailScreen
import com.example.recipeapp.ui.theme.RecipeAppTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val mealId = intent.getStringExtra("MEAL_ID") ?: ""
        
        setContent {
            RecipeAppTheme {
                DetailScreen(
                    mealId = mealId,
                    onBackClick = { finish() }
                )
            }
        }
    }
}
