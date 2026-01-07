package ru.itis.terraapp.feature.attractions.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.feature.attractions.R
import ru.itis.terraapp.feature.attractions.state.TempDetailsEffect
import ru.itis.terraapp.feature.attractions.viewModel.MainScreenViewModel


@Composable
fun AttractionDetailScreenRoute(
    viewModel: MainScreenViewModel,
    attractionId: String
) {
    val attractions by viewModel.uiState.collectAsState()
    val attraction = attractions.attractions.find { it.id == attractionId }

    if (attraction == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.attraction_not_found),
                color = Color.Red,
                fontSize = 18.sp
            )
        }
        return
    }

    AttractionDetailsScreenContent(attraction = attraction)
}

@Composable
fun AttractionDetailsScreenContent(attraction: Attraction) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
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
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
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
                            text = "${stringResource(R.string.category_label)} ${attraction.category}",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "${stringResource(R.string.rating_label)}  ${attraction.rating}",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "${stringResource(R.string.address_label)}  ${attraction.address}",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = stringResource(R.string.description),
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