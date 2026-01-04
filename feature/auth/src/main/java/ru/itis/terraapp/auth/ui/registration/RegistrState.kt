package ru.itis.terraapp.auth.ui.registration

data class RegistrState (
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val registerResult: Boolean = false,
)

sealed class RegistrEvent{
    data class UsernameUpdate(val username: String): RegistrEvent()
    data class EmailUpdate(val email: String): RegistrEvent()
    data class PasswordUpdate(val password: String): RegistrEvent()
    data object RegisterBtnClicked: RegistrEvent()
    data object LoginBtnClicked: RegistrEvent()
}

sealed interface RegistrEffect{
    data object NavigateToAuthorization: RegistrEffect
    data object NavigateToCurrentTemp: RegistrEffect
    data class ShowToast(val message: String): RegistrEffect
}