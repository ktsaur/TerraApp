package ru.itis.terraapp.domain.usecase.favourites

import ru.itis.terraapp.domain.model.Attraction
import ru.itis.terraapp.domain.repositories.FavouriteAttractionsRepository
import javax.inject.Inject

class GetFavouriteAttractionsUseCase @Inject constructor(
    private val favouriteAttractionsRepository: FavouriteAttractionsRepository
) {
    suspend operator fun invoke(): List<Attraction> {
        return favouriteAttractionsRepository.getFavourites()
    }
}


