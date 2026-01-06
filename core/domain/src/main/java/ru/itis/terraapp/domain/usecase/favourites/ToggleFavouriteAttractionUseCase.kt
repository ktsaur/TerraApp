package ru.itis.terraapp.domain.usecase.favourites

import android.util.Log
import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.FavouriteAttractionsRepository
import javax.inject.Inject

class ToggleFavouriteAttractionUseCase @Inject constructor(
    private val favouriteAttractionsRepository: FavouriteAttractionsRepository
) {

    /**
     * Возвращает новое состояние избранного после переключения.
     */
    suspend operator fun invoke(attraction: Attraction): Boolean {
        val isCurrentlyFavourite = favouriteAttractionsRepository.isFavourite(attraction.id)
        if (isCurrentlyFavourite) {
            favouriteAttractionsRepository.removeFromFavourites(attraction.id)
            Log.i("FAVOURITE", "Удалили из избранных")
            return false
        } else {
            favouriteAttractionsRepository.addToFavourites(attraction)
            Log.i("FAVOURITE", "Добавили в избранные")
            return true
        }
    }
}


