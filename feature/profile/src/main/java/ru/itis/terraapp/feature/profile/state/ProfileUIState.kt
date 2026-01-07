package ru.itis.terraapp.feature.profile.state

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ProfileUIState(
    val username: String = "",
    val email: String = "",
    val showLogoutDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ProfileEffect {
    data object NavigateToLogin: ProfileEffect
    data class ShowToast(val message: String): ProfileEffect
}

sealed interface ProfileUIEvent {
    data object ClickLogoutBtn : ProfileUIEvent
    data object ConfirmLogout : ProfileUIEvent
    data object DismissLogoutDialog : ProfileUIEvent
    data object ClickDeleteBtn : ProfileUIEvent
    data class ShowError(val message: String) : ProfileUIEvent
    data object ClearError : ProfileUIEvent
}


