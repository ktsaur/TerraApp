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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.itis.terraapp.feature.profile.state.ProfileEffect
import ru.itis.terraapp.feature.profile.state.ProfileUIEvent
import ru.itis.terraapp.feature.profile.state.ProfileUIState
import ru.itis.terraapp.feature.profile.viewModel.ProfileViewModel

@Composable
fun ProfileScreenContent() {
    Scaffold { padding ->
        Text("Profile")
    }
}

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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = "Профиль",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ProfileInfoRow(label = "Имя пользователя", value = state.username)
                        ProfileInfoRow(label = "Email", value = state.email)
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onEvent(ProfileUIEvent.ClickLogoutBtn) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color(0xFF4A6FA5))
                    ) {
                        Text(
                            text = "Выйти из аккаунта",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = {
                             onEvent(ProfileUIEvent.ClickDeleteBtn)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE63946),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Удалить аккаунт",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (state.showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { onEvent(ProfileUIEvent.DismissLogoutDialog) },
                    title = {
                        Text("Выйти из аккаунта?")
                    },
                    text = {
                        Text("Вы уверены, что хотите выйти? Вы будете перенаправлены на экран входа.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { onEvent(ProfileUIEvent.ConfirmLogout) }
                        ) {
                            Text("Выйти", color = Color(0xFFE63946))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { onEvent(ProfileUIEvent.DismissLogoutDialog) }
                        ) {
                            Text("Отмена")
                        }
                    },
                    containerColor = Color(0xFF1A2D52),
                    titleContentColor = Color.White,
                    textContentColor = Color.White
                )
            }

            state.errorMessage?.let { message ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(message)
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFF8A9BBA)
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

