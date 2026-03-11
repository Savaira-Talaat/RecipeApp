package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.example.recipeapp.activity.DetailActivity
import com.example.recipeapp.ui.screen.RecipeAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
                RecipeAppScreen(
                    onMealClick = { mealId ->
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("MEAL_ID", mealId)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
