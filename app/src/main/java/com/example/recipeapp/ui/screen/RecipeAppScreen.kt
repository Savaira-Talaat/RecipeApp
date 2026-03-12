package com.example.recipeapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Meal
import com.example.recipeapp.ui.component.MealItem
import com.example.recipeapp.ui.theme.Purple40
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.example.recipeapp.util.Result
import com.example.recipeapp.viewmodel.MealViewModel

@Composable
fun RecipeAppScreen(
    mealViewModel: MealViewModel = viewModel(),
    onMealClick: (String) -> Unit
) {
    val mealsState = mealViewModel.mealsState
    val isLoadingMore = mealViewModel.isLoadingMore
    // Récupère l'état des filtres
    val categoriesState = mealViewModel.categoriesState
    val selectedCategory = mealViewModel.selectedCategory

    LaunchedEffect(Unit) {
        if (mealsState is Result.Loading) {
            mealViewModel.loadRandomMeals()
        }
    }

    RecipeAppContent(
        mealsState = mealsState,
        isLoadingMore = isLoadingMore,
        categoriesState = categoriesState,
        selectedCategory = selectedCategory,
        onSearch = { query ->
            if (query.isEmpty()) mealViewModel.loadRandomMeals()
            else mealViewModel.searchMeals(query)
        },
        onRefresh = { mealViewModel.loadRandomMeals() },
        onLoadMore = { mealViewModel.loadRandomMeals(clear = false) },
        // Délègue le clic au ViewModel
        onCategoryClick = { category -> mealViewModel.toggleCategoryFilter(category) },
        onMealClick = onMealClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAppContent(
    mealsState: Result<List<Meal>>,
    isLoadingMore: Boolean,
    // Nouveaux paramètres pour les filtres
    categoriesState: Result<List<Category>>,
    selectedCategory: String?,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onMealClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val gridState = rememberLazyGridState()
    // État du scroll horizontal des chips
    val categoryScrollState = rememberScrollState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItemsCount = gridState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        // Pas de scroll infini si filtre actif
        if (shouldLoadMore.value && mealsState is Result.Success && !isLoadingMore && selectedCategory == null) {
            onLoadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                ),
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { newQuery ->
                            query = newQuery
                            onSearch(newQuery)
                        },
                        placeholder = { Text("Rechercher une recette...", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth(0.95f)
                            .height(55.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(30.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.White.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            query = ""
                            onRefresh()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        // Column pour empiler chips + liste
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            // Barre de chips scrollable horizontalement
            if (categoriesState is Result.Success && categoriesState.data.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Purple40)
                        .horizontalScroll(categoryScrollState)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesState.data.forEach { category ->
                        // Chip sélectionné = blanc, non sélectionné = transparent
                        val isSelected = selectedCategory == category.name
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategoryClick(category.name) },
                            label = { Text(category.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White.copy(alpha = 0.15f),
                                labelColor = Color.White,
                                selectedContainerColor = Color.White,
                                selectedLabelColor = Purple40
                            )
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (mealsState) {
                    is Result.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFCE4EC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = Purple40)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "chargement", color = Color.Black)
                            }
                        }
                    }
                    is Result.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = mealsState.message)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { onRefresh() }) {
                                Text("Retry")
                            }
                        }
                    }
                    is Result.Success -> {
                        LazyVerticalGrid(
                            state = gridState,
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(mealsState.data) { meal ->
                                MealItem(meal = meal) {
                                    onMealClick(meal.id)
                                }
                            }

                            // Indicateur de chargement en bas de la grille
                            if (isLoadingMore) {
                                item(span = { GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFFCE4EC))
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "chargement", color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeAppScreenPreview() {
    RecipeAppTheme {
        val dummyMeals = listOf(
            Meal("1", "Chicken Teriyaki", "Chicken", "Japanese", "https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg"),
            Meal("2", "Beef Wellington", "Beef", "British", "https://www.themealdb.com/images/media/meals/vvpprx1488325699.jpg"),
            Meal("3", "Spaghetti Carbonara", "Pasta", "Italian", "https://www.themealdb.com/images/media/meals/llc9441618620838.jpg"),
            Meal("4", "Tacos", "Beef", "Mexican", "https://www.themealdb.com/images/media/meals/uvuyxu1503067369.jpg")
        )
        val dummyCategories = listOf(
            Category("1", "Beef", null),
            Category("2", "Chicken", null),
            Category("3", "Seafood", null),
        )
        RecipeAppContent(
            mealsState = Result.Success(dummyMeals),
            isLoadingMore = false,
            categoriesState = Result.Success(dummyCategories),
            selectedCategory = "Chicken",
            onSearch = {},
            onRefresh = {},
            onLoadMore = {},
            onCategoryClick = {},
            onMealClick = {}
        )
    }
}