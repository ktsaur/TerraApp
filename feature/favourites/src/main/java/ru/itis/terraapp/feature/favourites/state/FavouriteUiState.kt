package ru.itis.terraapp.feature.favourites.state

import ru.itis.terraapp.domain.model.Attraction

data class FavouriteUiState(
    val isLoading: Boolean = true,
    val favourites: List<Attraction> = emptyList(),
    val attractions: List<Attraction> = emptyList(),
    val error: Throwable? = null
)

sealed interface FavouriteEffect {
    data class NavigateToAttractionDetails(val attractionId: String) : FavouriteEffect
}

sealed class FavouriteEvent {
    data class AttractionClicked(val attractionId: String): FavouriteEvent()
}

