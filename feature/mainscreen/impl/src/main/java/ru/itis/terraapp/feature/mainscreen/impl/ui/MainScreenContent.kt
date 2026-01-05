package ru.itis.terraapp.feature.mainscreen.impl.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.terraapp.feature.mainscreen.impl.state.TempDetailsEffect
import ru.itis.terraapp.feature.mainscreen.impl.state.TempDetailsEvent
import ru.itis.terraapp.feature.mainscreen.impl.state.WeatherUIState
import ru.itis.terraapp.feature.mainscreen.impl.viewModel.TempDetailsViewModel

@Composable
fun CurrentTempRoute(
    onNavigateToDetails: (String) -> Unit,  // приходит из app
    viewModel: TempDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    CurrentTempScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

    // Если всё-таки хочешь effects:
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is TempDetailsEffect.NavigateToTempDetails -> onNavigateToDetails(effect.city)
                // другие effects обрабатывай здесь или передавай отдельными callbacks
                else -> {}
            }
        }
    }
}

@Composable
fun CurrentTempScreen(
    uiState: WeatherUIState,
    onEvent: (TempDetailsEvent) -> Unit
) {
    CurrentTempFragmentContent(state = uiState, onEvent = onEvent)
}

@Composable
fun CurrentTempFragmentContent(
    state: WeatherUIState, onEvent: (TempDetailsEvent) -> Unit
) {
    Scaffold { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                value = state.city,
                onValueChange = {
                    onEvent(TempDetailsEvent.CityUpdate(city = it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Enter city",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 5.dp)
            )
            OutlinedButton(
                onClick = {
                    onEvent(TempDetailsEvent.GetWeatherBtnClicked)
                },
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Text(text = "Request")
            }
        }
    }
}

