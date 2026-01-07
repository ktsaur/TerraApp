package ru.itis.terraapp.feature.attractions.ui

import android.content.Context
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import retrofit2.HttpException
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.feature.attractions.R
import ru.itis.terraapp.feature.attractions.state.TempDetailsEffect
import ru.itis.terraapp.feature.attractions.state.TempDetailsEvent
import ru.itis.terraapp.feature.attractions.state.WeatherUIState
import ru.itis.terraapp.feature.attractions.util.CityValidationException
import ru.itis.terraapp.feature.attractions.viewModel.MainScreenViewModel
import java.io.IOException

@Composable
fun AttractionsScreenRoute(
    onNavigate: (TempDetailsEffect) -> Unit,
    viewModel: MainScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect {effect ->
            onNavigate(effect)
        }
    }
    AttractionsScreenContent(state = uiState, onEvent = viewModel::onEvent, onNavigateToAttraction = onNavigate)
}

@Composable
fun AttractionsScreenContent(
    state: WeatherUIState,
    onEvent: (TempDetailsEvent) -> Unit,
    onNavigateToAttraction: (TempDetailsEffect) -> Unit
) {
    if (state.error != null) {
        ErrorAlertDialog(
            ex = state.error,
            onConfirmBack = {
                onEvent(TempDetailsEvent.OnErrorConfirm)
            },
            context = LocalContext.current
        )
        return
    }
    if (state.isLoading || state.forecast.isNullOrEmpty()) {
        ShimmerScreen()
        return
    }
    val tomorrowAndDayAfter = getTomorrowAndDayAfterForecast(state.forecast)

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TempMainCard(
                    city = state.city,
                    weatherUIState = state
                )
            }
            item {
                TomorrowForecastSection(
                    tomorrowForecast = tomorrowAndDayAfter.first,
                    dayAfterForecast = tomorrowAndDayAfter.second
                )
            }
            if (state.attractions.isNotEmpty()) {
                item {
                    Text(
                        text = "Достопримечательности",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp)
                    )
                }

                items(
                    items = state.attractions,
                    key = { it.id }
                ) { attraction ->
                    AttractionCard(
                        attraction = attraction,
                        onClick = {
                            onEvent(TempDetailsEvent.AttractionClicked(attraction.id))
                        },
                        onFavouriteClick = {
                            onEvent(TempDetailsEvent.FavouriteToggle(attraction.id))
                        },
                        isFavourite = state.favouriteAttractionIds.contains(attraction.id)
                    )
                }
            }
            /*item {
                if (state.attractions.isNotEmpty()) {
                    AttractionsSection(
                        attractions = state.attractions,
                        onAttractionClick = { attractionId ->
                            onEvent(TempDetailsEvent.AttractionClicked(attractionId))
                        }
                    )
                }
            }*/
        }
    }
}

@Composable
fun ShimmerScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .shimmerEffect(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = Color.White)
                )
            }
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .shimmerEffect(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.elevatedCardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(4) {
                                Card(
                                    modifier = Modifier
                                        .size(58.dp)
                                        .shimmerEffect(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Blue
                                    )
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastSection(title: String, forecast: List<Forecast>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Card(
            modifier = Modifier.padding(top = 30.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(forecast) { _, item ->
                    ItemRow(forecastModel = item)
                }
            }
        }
    }
}

@Composable
fun ItemRow(forecastModel: Forecast) {
    var openDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .size(58.dp)
            .clickable { openDialog = true },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Blue
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = forecastModel.dt, fontSize = 14.sp)
                Text(
                    text = "${forecastModel.temp.toInt()}°C",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            title = { Text(text = stringResource(id = R.string.detail_temperature)) },
            text = {
                Text(
                    text = buildString {
                        appendLine(stringResource(R.string.time, forecastModel.dt))
                        appendLine(stringResource(R.string.temp, forecastModel.temp.toInt()))
                        appendLine(
                            stringResource(
                                R.string.feels_like,
                                forecastModel.feelsLike.toInt()
                            )
                        )
                        appendLine(stringResource(R.string.max_temp, forecastModel.tempMax.toInt()))
                        appendLine(stringResource(R.string.min_temp, forecastModel.tempMin.toInt()))
                        appendLine(stringResource(R.string.description, forecastModel.mainDesc))
                    }
                )
            }
        )
    }
}


@Composable
fun TempMainCard(city: String, weatherUIState: WeatherUIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.temp_in_city, city),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = "${weatherUIState.weather.currentTemp.toInt()}°C",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = weatherUIState.weather.weatherDescription,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}


@Composable
fun ErrorAlertDialog(ex: Throwable, context: Context, onConfirmBack: () -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { openDialog = false },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog = false
                    onConfirmBack()
                }
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        title = { Text(text = stringResource(id = R.string.error_title)) },
        text = { Text(text = getErrorMessage(ex = ex, context = context)) }
    )
}

/**
 * Получает прогноз на завтра и послезавтра на 12:00
 * @return Pair<Forecast для завтра, Forecast для послезавтра>
 */
private fun getTomorrowAndDayAfterForecast(forecast: List<Forecast>): Pair<Forecast?, Forecast?> {
    // Находим индексы полуночи (00:00) для разделения дней
    val midnightIndices = forecast.mapIndexedNotNull { index, forecastModel ->
        if (forecastModel.dt == "00:00") index else null
    }

    if (midnightIndices.size < 2) {
        // Если нет достаточно данных, возвращаем null
        return Pair(null, null)
    }

    // Первый день - от начала до первой полуночи
    // Второй день - от первой полуночи до второй
    // Третий день - от второй полуночи до третьей

    val tomorrowStartIndex = midnightIndices[0]
    val tomorrowEndIndex = if (midnightIndices.size > 1) midnightIndices[1] else forecast.size
    val dayAfterStartIndex = if (midnightIndices.size > 1) midnightIndices[1] else forecast.size
    val dayAfterEndIndex = if (midnightIndices.size > 2) midnightIndices[2] else forecast.size

    val tomorrowForecast = forecast.subList(tomorrowStartIndex, tomorrowEndIndex)
    val dayAfterForecast = forecast.subList(dayAfterStartIndex, dayAfterEndIndex)

    // Ищем температуру на 12:00 для каждого дня
    val tomorrowAt12 = tomorrowForecast.find { it.dt == "12:00" }
    val dayAfterAt12 = dayAfterForecast.find { it.dt == "12:00" }

    return Pair(tomorrowAt12, dayAfterAt12)
}

/*@Composable
fun TomorrowForecastSection(
    tomorrowForecast: Forecast?,
    dayAfterForecast: Forecast?
) {
    Column {
        Text(
            text = stringResource(id = R.string.forecast_tomorrow_day_after),
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Блок "Завтра"
                if (tomorrowForecast != null) {
                    TomorrowDayCard(
                        title = stringResource(id = R.string.tomorrow),
                        forecast = tomorrowForecast
                    )
                }

                // Блок "Послезавтра"
                if (dayAfterForecast != null) {
                    TomorrowDayCard(
                        title = stringResource(id = R.string.day_after_tomorrow),
                        forecast = dayAfterForecast
                    )
                }
            }
        }
    }
}*/

@Composable
fun TomorrowForecastSection(
    tomorrowForecast: Forecast?,
    dayAfterForecast: Forecast?
) {
    Column {
        Text(
            text = stringResource(id = R.string.forecast_tomorrow_day_after),
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (tomorrowForecast != null) {
                    TomorrowDayCard(
                        title = stringResource(id = R.string.tomorrow),
                        forecast = tomorrowForecast,
                        modifier = Modifier.weight(1f)  // ← weight здесь!
                    )
                }

                if (dayAfterForecast != null) {
                    TomorrowDayCard(
                        title = stringResource(id = R.string.day_after_tomorrow),
                        forecast = dayAfterForecast,
                        modifier = Modifier.weight(1f)  // ← и здесь!
                    )
                }
            }
        }
    }
}

@Composable
fun TomorrowDayCard(
    title: String,
    forecast: Forecast,
    modifier: Modifier = Modifier  // ← добавьте параметр modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),  // ← применяем modifier
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "${forecast.temp.toInt()}°C",
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

/*@Composable
fun TomorrowDayCard(title: String, forecast: Forecast) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "${forecast.temp.toInt()}°C",
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}*/

@Composable
fun AttractionsSection(
    attractions: List<Attraction>,
    onAttractionClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Column {
            Text(
                text = "Достопримечательности",
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(attractions) { attraction ->
                    AttractionCard(
                        attraction = attraction,
                        onClick = { onAttractionClick(attraction.id) },
                        onFavouriteClick = { /* TODO: handle here if section is used */ },
                        isFavourite = false
                    )
                }
            }
        }
    }
}

@Composable
fun AttractionCard(
    attraction: Attraction,
    onClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    isFavourite: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = attraction.imageUrl,
                contentDescription = attraction.name,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = attraction.name,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            IconButton(
                onClick = onFavouriteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavourite) R.drawable.ic_favourite_filled else R.drawable.ic_favourite_outline
                    ),
                    contentDescription = if (isFavourite) "Убрать из избранного" else "Добавить в избранное",
                    tint = if (isFavourite) Color.Red else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

private fun getErrorMessage(ex: Throwable, context: Context): String {
    return when (ex) {
        is HttpException -> {
            when (ex.code()) {
                401 -> context.getString(R.string.error_authentication)
                404 -> context.getString(R.string.error_city_not_found)
                else -> context.getString(R.string.error_server, ex.code())
            }
        }

        is IOException -> context.getString(R.string.error_connection)
        is CityValidationException -> context.getString(R.string.error_invalid_capitalization)
        else -> context.getString(R.string.error_unknown)
    }
}
