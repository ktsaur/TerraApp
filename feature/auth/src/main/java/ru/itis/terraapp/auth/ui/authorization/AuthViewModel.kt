package ru.itis.terraapp.auth.ui.authorization

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
import org.mindrot.jbcrypt.BCrypt
import ru.itis.terraapp.auth.R
import ru.itis.terraapp.auth.domain.repository.UserRepository
import ru.itis.terraapp.auth.utils.AuthManager
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = AuthorizationState())
    val uiSate = _uiState.asStateFlow()

    private val _effectFlow = MutableSharedFlow<AuthorizationEffect>()
    val effectFlow = _effectFlow.asSharedFlow()

    fun onEvent(event: AuthorizationEvent, context: Context) {
        when(event) {
            is AuthorizationEvent.EmailUpdate ->
                _uiState.update { it.copy(email = event.email) }
            is AuthorizationEvent.PasswordUpdate ->
                _uiState.update { it.copy(password = event.password) }
            is AuthorizationEvent.AuthorizationBtnClicked -> {
                authUser(context = context)
            }
            is AuthorizationEvent.RegistrationTextBtnClicked -> {
                viewModelScope.launch {
                    _effectFlow.emit(AuthorizationEffect.NavigateToRegister)
                }
            }
        }
    }

    fun authUser(context: Context) {
        val email = _uiState.value.email
        val password = _uiState.value.password
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email = email)
            if (user != null) {
                val storedHash = user.password
                if (BCrypt.checkpw(password, storedHash)) {
                    _uiState.update { it.copy(registerResult = true) }
                    user.userId?.let { authManager.saveUserId(it) }
                    _effectFlow.emit(AuthorizationEffect.NavigateToCurrentTemp)
                    _effectFlow.emit(AuthorizationEffect.ShowToast( message = context.getString(R.string.auth_success)))
                } else {
                    _uiState.update { it.copy(registerResult = false) }
                    _effectFlow.emit(AuthorizationEffect.ShowToast(message = context.getString(R.string.auth_wrong_password)))
                }
            } else {
                _uiState.update { it.copy(registerResult = false) }
                _effectFlow.emit(AuthorizationEffect.ShowToast(message = context.getString(R.string.auth_user_not_found)))
            }
        }
    }
}