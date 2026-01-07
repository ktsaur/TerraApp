package ru.itis.terraapp.feature.attractions.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.itis.terraapp.feature.attractions.R
import ru.itis.terraapp.feature.attractions.state.TempDetailsEffect
import ru.itis.terraapp.feature.attractions.state.TempDetailsEvent
import ru.itis.terraapp.feature.attractions.state.WeatherUIState
import ru.itis.terraapp.feature.attractions.viewModel.MainScreenViewModel

@Composable
fun MainScreenRoute(
    onNavigate: (TempDetailsEffect) -> Unit,
    viewModel: MainScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect {effect ->
            onNavigate(effect)
        }
    }

    MainScreenFragmentContent(state = uiState, onEvent = viewModel::onEvent)
}


@Composable
fun MainScreenFragmentContent(
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
                text = stringResource(R.string.enter_city),
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
                Text(text = stringResource(R.string.request))
            }
        }
    }
}