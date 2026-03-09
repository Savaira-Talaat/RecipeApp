package com.example.recipeapp.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.model.Meal
import com.example.recipeapp.ui.theme.Purple40

@Composable
fun MealItem(
    meal: Meal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Purple40, contentColor = Color.White),
        border = BorderStroke(color = Purple40, width = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AsyncImage(
                model = meal.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                placeholder = painterResource(id = R.drawable.ic_launcher_background), // Fallback to a default resource
            )

            Text(
                text = meal.title,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
                    .fillMaxWidth().height(50.dp),
            )
        }
    }
}
