package ru.itis.terraapp.navigation

import android.util.Log
import androidx.navigation.NavController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun clearNavController() {
        navController = null
    }

    fun getCurrentRoute(): String? {
        val route = navController?.currentBackStackEntry?.destination?.route
        Log.d("NavigationManager", "getCurrentRoute: текущий route = $route")
        return route
    }

    fun navigate(route: String) {
        if (navController == null) {
            return
        }

        navController?.let { controller ->
            try {
                val currentRoute = getCurrentRoute()
                if (currentRoute == route) {
                    return
                }
                controller.navigate(route)
            } catch (e: Exception) {
                Log.e("NavigationManager", "navigate: ошибка при навигации", e)
            }
        }
    }
}