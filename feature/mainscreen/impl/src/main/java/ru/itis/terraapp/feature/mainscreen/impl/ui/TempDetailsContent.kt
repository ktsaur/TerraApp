package ru.itis.terraapp.feature.mainscreen.impl.ui

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.HttpException
import ru.itis.terraapp.domain.model.Forecast
import ru.itis.terraapp.feature.mainscreen.impl.R
import ru.itis.terraapp.feature.mainscreen.impl.state.TempDetailsEffect
import ru.itis.terraapp.feature.mainscreen.impl.state.TempDetailsEvent
import ru.itis.terraapp.feature.mainscreen.impl.state.WeatherUIState
import ru.itis.terraapp.feature.mainscreen.impl.util.CityValidationException
import ru.itis.terraapp.feature.mainscreen.impl.viewModel.TempDetailsViewModel
import java.io.IOException

@Composable
fun TempDetailsRoute(
    onNavigateToDetails: (String) -> Unit,  // приходит из app
    viewModel: TempDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    TempDetailsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is TempDetailsEffect.ShowToast -> {
                    //показать toast
                }
                is TempDetailsEffect.NavigateBack -> {
                    // Навигация назад обрабатывается через NavController в MainScreenApiImpl
                }
                else -> {}
            }
        }
    }
}

@Composable
fun TempDetailsScreen(
    uiState: WeatherUIState,
    onEvent: (TempDetailsEvent) -> Unit
) {
    TempDetailsContent(state = uiState, onEvent = onEvent)
}

@Composable
fun TempDetailsContent(state: WeatherUIState, onEvent: (TempDetailsEvent) -> Unit) {
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
    val listForecasts = splitForecast(state.forecast)

    Scaffold(containerColor = Color.Blue) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TempMainCard(
                city = state.city,
                weatherUIState = state
            )
            ForecastSection(
                title = stringResource(id = R.string.first_day),
                forecast = listForecasts[0]
            )
            ForecastSection(
                title = stringResource(id = R.string.second_day),
                forecast = listForecasts[1]
            )
            ForecastSection(
                title = stringResource(id = R.string.third_day),
                forecast = listForecasts[2]
            )
        }
    }
}

@Composable
fun ShimmerScreen() {
    Scaffold(containerColor = Color.Blue) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 68.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.temp_in_city, city),
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = "${weatherUIState.weather.currentTemp.toInt()}°C",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(top = 20.dp)
                )
                Text(
                    text = weatherUIState.weather.weatherDescription,
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
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

private fun splitForecast(forecast: List<Forecast>): List<List<Forecast>> {
    val indicesOfMidnight = forecast.mapIndexedNotNull { index, forecastModel ->
        if (forecastModel.dt == "00:00") index else null
    }
    return listOf(
        forecast.take(indicesOfMidnight[0]),
        forecast.drop(indicesOfMidnight[0]).take(indicesOfMidnight[1] - indicesOfMidnight[0]),
        forecast.drop(indicesOfMidnight[1]).take(indicesOfMidnight[2] - indicesOfMidnight[1])
    )
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
