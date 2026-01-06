package ru.itis.terraapp.feature.favourites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.terraapp.base.AuthManager.AuthManager
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.usecase.favourites.GetFavouriteAttractionsUseCase
import javax.inject.Inject

@HiltViewModel
class FavouriteScreenViewModel @Inject constructor(
    private val getFavouriteAttractionsUseCase: GetFavouriteAttractionsUseCase,
    private val authManager: AuthManager
): ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val favourites: List<Attraction> = emptyList(),
        val error: Throwable? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _effectFlow = MutableSharedFlow<Unit>()
    val effectFlow = _effectFlow.asSharedFlow()

    fun loadFavourites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching {
                val userId = authManager.getUserId() ?: throw IllegalStateException("User not logged in")
                getFavouriteAttractionsUseCase(userId)
            }.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    favourites = list,
                    error = null
                )
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = ex
                )
            }
        }
    }
}