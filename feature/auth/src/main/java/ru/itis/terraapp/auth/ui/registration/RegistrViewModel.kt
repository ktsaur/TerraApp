package ru.itis.terraapp.auth.ui.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.terraapp.auth.R
import ru.itis.terraapp.auth.utils.hashPassword
import ru.itis.terraapp.base.AuthManager.AuthManager
import ru.itis.terraapp.domain.usecase.auth.GetEmailsUseCase
import ru.itis.terraapp.domain.usecase.auth.RegisterUserUseCase
import javax.inject.Inject

@HiltViewModel
class RegistrViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getEmailsUseCase: GetEmailsUseCase,
    private val authManager: AuthManager
): ViewModel() {

    private val _uiState = MutableStateFlow(RegistrState())
    val uiState = _uiState.asStateFlow()

    private val _effectFlow = MutableSharedFlow<RegistrEffect>()
    val effectFlow = _effectFlow.asSharedFlow()

    fun onEvent(event: RegistrEvent, context: Context) {
        when(event) {
            is RegistrEvent.UsernameUpdate -> {
                _uiState.update { it.copy(username = event.username) }
            }
            is RegistrEvent.EmailUpdate -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is RegistrEvent.PasswordUpdate -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is RegistrEvent.RegisterBtnClicked -> {
                register(context = context)
            }
            is RegistrEvent.LoginBtnClicked -> {
                viewModelScope.launch {
                    _effectFlow.emit(RegistrEffect.NavigateToAuthorization)
                }
            }
        }
    }

    fun register(context: Context) {
        val username = _uiState.value.username
        val email = _uiState.value.email
        val password = _uiState.value.password

        val hashPassword = hashPassword(password)

        viewModelScope.launch {
            if (username != "" && email != "" && password != "") {
                val listEmails = getEmailsUseCase.invoke()
                if (listEmails.contains(email) == true) {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.email_taken)))
                    return@launch
                }
                if(!email.contains("@")) {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_invalid_email)))
                    return@launch
                }
                if (password.length < 6 || !isValidPassword(password = password)) {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_invalid_password)))
                    return@launch
                }
            } else {
                _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_fill_all_fields)))
                return@launch
            }
            val userId = registerUserUseCase.invoke(username = username, email = email, password = hashPassword)
            authManager.saveUserId(userId)
            _uiState.update { it.copy(registerResult = true) }
            _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.success_registration)))
            _effectFlow.tryEmit(RegistrEffect.NavigateToCurrentTemp)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d).+$"
        return password.matches(passwordPattern.toRegex())
    }
}