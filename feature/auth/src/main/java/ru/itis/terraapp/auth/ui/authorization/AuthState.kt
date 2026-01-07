package ru.itis.terraapp.auth.ui.authorization

data class AuthorizationState (
    val email: String = "",
    val password: String = "",
    val registerResult: Boolean = false,
)

sealed class AuthorizationEvent{
    data class EmailUpdate(val email: String): AuthorizationEvent()
    data class PasswordUpdate(val password: String): AuthorizationEvent()
    data object AuthorizationBtnClicked: AuthorizationEvent()
    data object RegistrationTextBtnClicked: AuthorizationEvent()
}

sealed interface AuthorizationEffect{
    data object NavigateToRegister: AuthorizationEffect
    data object NavigateToCurrentTemp: AuthorizationEffect
    data class ShowToast(val message: String): AuthorizationEffect
}