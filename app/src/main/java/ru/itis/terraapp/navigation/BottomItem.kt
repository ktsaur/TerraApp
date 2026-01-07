package ru.itis.terraapp.navigation

import ru.itis.terraapp.R

sealed class BottomItem(val title: String, val iconId: Int, val road: String) {
    object MainScreen: BottomItem(title = "Main", iconId = R.drawable.home, road = Screen.MainScreen.route)
    object FavouritesScreen: BottomItem(title = "Favourites", iconId = R.drawable.favourites, road = Screen.FavouritesScreen.route)
    object ProfileScreen: BottomItem(title = "Profile", iconId = R.drawable.profile, road = Screen.ProfileScreen.route)
}