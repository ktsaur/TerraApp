package ru.itis.terraapp.feature.profile.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.terraapp.base.AuthManager.AuthManager
import ru.itis.terraapp.base.util.AnalyticsHelper
import ru.itis.terraapp.domain.usecase.profile.DeleteUserUseCase
import ru.itis.terraapp.domain.usecase.profile.GetUserUseCase
import ru.itis.terraapp.feature.profile.state.ProfileEffect
import ru.itis.terraapp.feature.profile.state.ProfileUIEvent
import ru.itis.terraapp.feature.profile.state.ProfileUIState
import ru.itis.terraapp.feature.profile.util.runSuspendCatching
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val authManager: AuthManager,
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState = _uiState.asStateFlow()

    private val _effectFlow = MutableSharedFlow<ProfileEffect>()
    val effectFlow = _effectFlow.asSharedFlow()

    init {
        loadUserData()
        logFavouriteScreenView()
    }

    fun onEvent(event: ProfileUIEvent) {
        when (event) {
            is ProfileUIEvent.ClickLogoutBtn -> {
                _uiState.update { it.copy(showLogoutDialog = true) }
            }

            is ProfileUIEvent.ConfirmLogout -> {
                _uiState.update { it.copy(showLogoutDialog = false) }
                authManager.clearUserId()
                viewModelScope.launch {
                    _effectFlow.emit(ProfileEffect.NavigateToLogin)
                    _effectFlow.emit(ProfileEffect.ShowToast("Вы вышли из аккаунта"))
                }
            }

            is ProfileUIEvent.DismissLogoutDialog -> {
                _uiState.update { it.copy(showLogoutDialog = false) }
            }

            is ProfileUIEvent.ShowError -> {
                _uiState.update { it.copy(errorMessage = event.message) }
            }

            is ProfileUIEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            ProfileUIEvent.ClickDeleteBtn -> {
                deleteUser()
                _uiState.update { it.copy(showLogoutDialog = false) }
                authManager.clearUserId()
                viewModelScope.launch {
                    _effectFlow.emit(ProfileEffect.NavigateToLogin)
                    _effectFlow.emit(ProfileEffect.ShowToast("Вы удалили из аккаунт"))
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runSuspendCatching {
                val userId = authManager.getUserId()
                val user = getUserUseCase.invoke(userId = userId)
                user
            }.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        username = user.username,
                        email = user.email,
                        isLoading = false,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Невозможно загрузить профиль"
                    )
                }
            }
        }
    }

    private fun deleteUser(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runSuspendCatching {
                val userId = authManager.getUserId()
                deleteUserUseCase.invoke(userId = userId)
            }.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Невозможно удалить профиль"
                    )
                }
            }
        }
    }

    fun logFavouriteScreenView() {
        Log.i("fav", "до")
        AnalyticsHelper.logScreenView(
            screenName = "Favourites",
            screenClass = "FavouriteScreenContent"
        )
        Log.i("fav", "после")
    }
}