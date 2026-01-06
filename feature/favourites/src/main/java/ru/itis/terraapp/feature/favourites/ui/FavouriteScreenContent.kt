package ru.itis.terraapp.feature.favourites.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FavouriteScreenContent() {
    Scaffold { paddingValue ->
        Text(text = "Избранные")
    }
}