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
    suspend operator fun invoke(attraction: Attraction, userId: Int): Boolean {
        val isCurrentlyFavourite = favouriteAttractionsRepository.isFavourite(attraction.id, userId)
        if (isCurrentlyFavourite) {
            favouriteAttractionsRepository.removeFromFavourites(attraction.id, userId)
            Log.i("FAVOURITE", "Удалили из избранных")
            return false
        } else {
            favouriteAttractionsRepository.addToFavourites(attraction, userId)
            Log.i("FAVOURITE", "Добавили в избранные")
            return true
        }
    }
}


