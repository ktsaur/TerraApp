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
import ru.itis.terraapp.auth.utils.AuthManager
import ru.itis.terraapp.domain.usecase.auth.RegisterResult
import ru.itis.terraapp.domain.usecase.auth.RegisterUserUseCase
import javax.inject.Inject

@HiltViewModel
class RegistrViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
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

        viewModelScope.launch {
            when (val result = registerUserUseCase(username, email, password)) {
                is RegisterResult.Success -> {
                    authManager.saveUserId(result.userId)
                    _uiState.update { it.copy(registerResult = true) }
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.success_registration)))
                    _effectFlow.tryEmit(RegistrEffect.NavigateToCurrentTemp)
                }
                is RegisterResult.EmptyFields -> {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_fill_all_fields)))
                }
                is RegisterResult.InvalidEmail -> {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_invalid_email)))
                }
                is RegisterResult.InvalidPassword -> {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_invalid_password)))
                }
                is RegisterResult.EmailTaken -> {
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.email_taken)))
                }
                is RegisterResult.UserAlreadyExists -> {
                    _uiState.update { it.copy(registerResult = false) }
                    _effectFlow.emit(RegistrEffect.ShowToast(message = context.getString(R.string.error_user_already_registered)))
                }
            }
        }
    }
}