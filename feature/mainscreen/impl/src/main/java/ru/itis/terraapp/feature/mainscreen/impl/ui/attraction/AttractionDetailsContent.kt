package ru.itis.terraapp.feature.mainscreen.impl.ui.attraction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.itis.terraapp.domain.model.Attraction

/*@Composable
fun AttractionDetailsScreen(attraction: Attraction) {
    Scaffold(containerColor = Color.Blue) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Изображение
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                AsyncImage(
                    model = attraction.imageUrl,
                    contentDescription = attraction.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Информация
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Название
                    Text(
                        text = attraction.name,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Категория
                    Text(
                        text = "Категория: ${attraction.category}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Рейтинг
                    Text(
                        text = "Рейтинг: ${attraction.rating}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Адрес
                    Text(
                        text = "Адрес: ${attraction.address}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Описание
                    Text(
                        text = "Описание:",
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = attraction.description,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}*/

@Composable
fun AttractionDetailsScreen(attraction: Attraction) {
    Scaffold(containerColor = Color.Blue) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)  // Обязательно для Scaffold
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Изображение
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    AsyncImage(
                        model = attraction.imageUrl,
                        contentDescription = attraction.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),  // Задайте фиксированную высоту или aspectRatio
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                // Информация
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = attraction.name,
                            style = TextStyle(
                                color = Color.DarkGray,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = "Категория: ${attraction.category}",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Рейтинг: ${attraction.rating}",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Адрес: ${attraction.address}",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Описание:",
                            style = TextStyle(color = Color.DarkGray, fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = attraction.description,
                            style = TextStyle(color = Color.DarkGray, fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }
}

