package ru.itis.terraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.terraapp.auth.utils.AuthManager
import ru.itis.terraapp.feature.mainscreen.api.MainScreenFeature
import ru.itis.terraapp.navigation.BottomNavigation
import ru.itis.terraapp.navigation.NavGraph
import ru.itis.terraapp.navigation.NavigationManager
import ru.itis.terraapp.navigation.Routes
import ru.itis.terraapp.navigation.Screen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var navigationManager: NavigationManager
    
    @Inject
    lateinit var mainScreenFeature: MainScreenFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            navigationManager.setNavController(navController)
            val userId = authManager.getUserId()
            val startDestination = if (userId != null && userId != -1) {
                Routes.MAIN_NAVIGATION
            } else {
                Routes.REGISTRATION
            }
            InitialNavigation(
                startDestination = startDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun InitialNavigation(
    startDestination: String,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNavigation = currentRoute in listOf(
        Screen.Graph.route, Screen.MainScreen.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomNavigation) {
                BottomNavigation(navController = navController)
            }
        }
    ) { padding ->
        NavGraph(
            navHostController = navController,
            startDestination = startDestination
        )
    }
}

