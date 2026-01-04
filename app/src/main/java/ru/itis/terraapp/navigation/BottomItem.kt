package ru.itis.terraapp.navigation

import ru.itis.terraapp.R

sealed class BottomItem(val title: String, val iconId: Int, val road: String) {
    object MainScreen: BottomItem(title = "Main", iconId = R.drawable.icon_weather, road = Screen.MainScreen.route)
}