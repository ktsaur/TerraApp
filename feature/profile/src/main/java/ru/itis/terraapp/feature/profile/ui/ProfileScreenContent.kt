package ru.itis.terraapp.feature.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.itis.terraapp.feature.profile.R
import ru.itis.terraapp.feature.profile.state.ProfileEffect
import ru.itis.terraapp.feature.profile.state.ProfileUIEvent
import ru.itis.terraapp.feature.profile.state.ProfileUIState
import ru.itis.terraapp.feature.profile.viewModel.ProfileViewModel

@Composable
fun ProfileRoute(
    onNavigate: (ProfileEffect) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect { effect ->
            onNavigate(effect)
        }
    }

    ProfileScreen(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProfileScreen(
    state: ProfileUIState,
    onEvent: (ProfileUIEvent) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.profile),
                modifier = Modifier.padding(top = 100.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 50.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.username),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = state.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.email),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = state.email,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            FilledTonalButton(
                onClick = { onEvent(ProfileUIEvent.ClickLogoutBtn) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 70.dp)
            ) {
                Text(
                    text = stringResource(R.string.Logout),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            TextButton(
                onClick = { onEvent(ProfileUIEvent.ClickDeleteBtn) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_account),
                    color = Color(0xFFE63946),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (state.showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { onEvent(ProfileUIEvent.DismissLogoutDialog) },
                title = { Text(stringResource(R.string.logout_dialog_title)) },
                text = { Text(stringResource(R.string.logout_dialog_message)) },
                confirmButton = {
                    TextButton(onClick = { onEvent(ProfileUIEvent.ConfirmLogout) }) {
                        Text(stringResource(R.string.logout_confirm), color = Color(0xFFE63946))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(ProfileUIEvent.DismissLogoutDialog) }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
        state.errorMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Snackbar {
                    Text(message)
                }
            }
        }
    }
}
