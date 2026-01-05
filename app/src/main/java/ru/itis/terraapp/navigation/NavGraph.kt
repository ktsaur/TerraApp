package ru.itis.terraapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.itis.terraapp.auth.ui.authorization.AuthRoute
import ru.itis.terraapp.auth.ui.authorization.AuthorizationEffect
import ru.itis.terraapp.auth.ui.registration.RegistrEffect
import ru.itis.terraapp.auth.ui.registration.RegistrRoute
import ru.itis.terraapp.feature.mainscreen.api.MainScreenFeature
import javax.inject.Inject

object Routes {
    const val AUTHORIZATION = "authorization"
    const val REGISTRATION = "registration"
    const val GRAPH = "graph"
    const val BOTTOM_GRAPH = "bottom_graph"
    const val MAIN_SCREEN = "main_screen"
}

sealed class Screen(val route: String) {
    object Graph : Screen(Routes.GRAPH)
    object Registration : Screen(Routes.REGISTRATION)
    object Authorization : Screen(Routes.AUTHORIZATION)
    object MainScreen : Screen(Routes.MAIN_SCREEN)
}

@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String,
    mainScreenFeature: MainScreenFeature
) {
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Authorization.route) {
            AuthRoute(
                onNavigate = { effect ->
                    when (effect) {
                        is AuthorizationEffect.NavigateToCurrentTemp -> {
                            navHostController.navigate(Routes.MAIN_SCREEN)
                        }

                        is AuthorizationEffect.NavigateToRegister -> {
                            navHostController.navigate(Screen.Registration.route)
                        }

                        is AuthorizationEffect.ShowToast -> Toast.makeText(
                            context,
                            effect.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
        composable(route = Screen.Registration.route) {
            RegistrRoute(
                onNavigate = { effect ->
                    when (effect) {
                        is RegistrEffect.NavigateToAuthorization -> {
                            navHostController.navigate(Screen.Authorization.route)
                        }

                        is RegistrEffect.NavigateToCurrentTemp -> {
                            navHostController.navigate(Routes.MAIN_SCREEN)
                        }

                        is RegistrEffect.ShowToast -> Toast.makeText(
                            context,
                            effect.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
        composable(route = Screen.MainScreen.route) {
            mainScreenFeature.mainScreenApi.MainScreen()
        }
    }
}