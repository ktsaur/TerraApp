package ru.itis.terraapp.feature.favourites.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.terraapp.base.AuthManager.AuthManager
import ru.itis.terraapp.base.util.AnalyticsHelper
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.usecase.favourites.GetFavouriteAttractionsUseCase
import ru.itis.terraapp.feature.favourites.state.FavouriteEffect
import ru.itis.terraapp.feature.favourites.state.FavouriteEvent
import ru.itis.terraapp.feature.favourites.state.FavouriteUiState
import javax.inject.Inject

@HiltViewModel
class FavouriteScreenViewModel @Inject constructor(
    private val getFavouriteAttractionsUseCase: GetFavouriteAttractionsUseCase,
    private val authManager: AuthManager
): ViewModel() {

    private val _uiState = MutableStateFlow(FavouriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _effectFlow = MutableSharedFlow<FavouriteEffect>()
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

    fun logFavouriteScreenView() {
        Log.i("fav", "до")
        AnalyticsHelper.logScreenView(
            screenName = "Favourites",
            screenClass = "FavouriteScreenContent"
        )
        Log.i("fav", "после")
    }
}