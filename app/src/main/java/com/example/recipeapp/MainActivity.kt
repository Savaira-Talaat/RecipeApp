package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.example.recipeapp.util.Result
import com.example.recipeapp.viewmodel.MealViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
//                val viewModel: MealViewModel = viewModel()
//
//                LaunchedEffect(Unit) {
//                    viewModel.searchMeals("")
//                }
//
//                when (val state = viewModel.mealsState) {
//                    is Result.Loading -> {
//                        Box(modifier = Modifier.fillMaxSize()) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//                    }
//                    is Result.Error -> {
//                        Column(
//                            modifier = Modifier.fillMaxSize(),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//                            Text(text = state.message)
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Button(onClick = { viewModel.searchMeals("") }) {
//                                Text("Réessayer")
//                            }
//                        }
//                    }
//                    is Result.Success -> {
//                        LazyColumn {
//                            items(state.data) { meal ->
//                                Text(
//                                    text = meal.title,
//                                    modifier = Modifier.padding(16.dp)
//                                )
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}