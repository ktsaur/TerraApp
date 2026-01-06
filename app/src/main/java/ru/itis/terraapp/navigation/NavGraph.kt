package ru.itis.terraapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.itis.terraapp.auth.ui.authorization.AuthRoute
import ru.itis.terraapp.auth.ui.authorization.AuthorizationEffect
import ru.itis.terraapp.auth.ui.registration.RegistrEffect
import ru.itis.terraapp.auth.ui.registration.RegistrRoute
import ru.itis.terraapp.feature.attractions.state.TempDetailsEffect
import ru.itis.terraapp.feature.attractions.ui.AttractionDetailScreenRoute
import ru.itis.terraapp.feature.attractions.ui.AttractionsScreenRoute
import ru.itis.terraapp.feature.attractions.ui.MainScreenRoute
import ru.itis.terraapp.feature.attractions.viewModel.MainScreenViewModel
import ru.itis.terraapp.feature.favourites.ui.FavouriteScreenContent
import ru.itis.terraapp.util.sharedViewModel

object Routes {
    const val AUTHORIZATION = "authorization"
    const val REGISTRATION = "registration"
    const val GRAPH = "graph"
    const val BOTTOM_GRAPH = "bottom_graph"
    const val MAIN_NAVIGATION = "main_nav"
    const val MAIN_SCREEN = "main_screen"
    const val ATTRACTIONS_SCREEN = "attr_screen"
    const val ATTRACTIONDETAIL_SCREEN = "attractiondetail_screen/{attractionId}"
    const val FAVOURITES_SCREEN = "fav_screen"
}

sealed class Screen(val route: String) {
    object Graph : Screen(Routes.GRAPH)
    object Registration : Screen(Routes.REGISTRATION)
    object Authorization : Screen(Routes.AUTHORIZATION)
    object MainScreen : Screen(Routes.MAIN_SCREEN)
    object AttractionsScreen : Screen(Routes.ATTRACTIONS_SCREEN)
    object FavouritesScreen : Screen(Routes.FAVOURITES_SCREEN)
    object AttractionDetailScreen : Screen(Routes.ATTRACTIONDETAIL_SCREEN)
}

@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String
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
                            navHostController.navigate(Routes.MAIN_NAVIGATION)
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
                            navHostController.navigate(Routes.MAIN_NAVIGATION)
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
        navigation(
            route = Routes.BOTTOM_GRAPH,
            startDestination = Routes.MAIN_NAVIGATION,
        ) {
            navigation(
                route = Routes.MAIN_NAVIGATION,
                startDestination = Screen.MainScreen.route
            ) {
                composable(route = Screen.MainScreen.route) { backStackEntry ->
                    val viewModel = backStackEntry.sharedViewModel<MainScreenViewModel>(
                        navController = navHostController,
                        navGraphRoute = Routes.MAIN_NAVIGATION,
                        navBackStackEntry = backStackEntry
                    )
                    MainScreenRoute(
                        onNavigate = { effect ->
                            when (effect) {
                                is TempDetailsEffect.NavigateToTempDetails -> {
                                    navHostController.navigate(Screen.AttractionsScreen.route)
                                }

                                is TempDetailsEffect.ShowToast -> Toast.makeText(
                                    context,
                                    effect.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                else -> {}
                            }
                        },
                        viewModel = viewModel
                    )
                }
                composable(route = Screen.AttractionsScreen.route) { backStackEntry ->
                    val viewModel = backStackEntry.sharedViewModel<MainScreenViewModel>(
                        navController = navHostController,
                        navGraphRoute = Routes.MAIN_NAVIGATION,
                        navBackStackEntry = backStackEntry
                    )
                    AttractionsScreenRoute(
                        onNavigate = { effect ->
                            when (effect) {
                                is TempDetailsEffect.NavigateToAttractionDetails -> {
                                    navHostController.navigate("attractiondetail_screen/${effect.attractionId}")
                                }
                                is TempDetailsEffect.ShowToast -> Toast.makeText(
                                    context,
                                    effect.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                else -> {}
                            }

                        }, viewModel = viewModel
                    )
                }
                composable(
                    route = "attractiondetail_screen/{attractionId}",
                    arguments = listOf(navArgument("attractionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val attractionId = backStackEntry.arguments?.getString("attractionId")!!

                    val viewModel = backStackEntry.sharedViewModel<MainScreenViewModel>(
                        navController = navHostController,
                        navGraphRoute = Routes.MAIN_NAVIGATION,
                        navBackStackEntry = backStackEntry
                    )

                    if (attractionId != null) {
                        AttractionDetailScreenRoute(viewModel = viewModel, attractionId = attractionId)
                    } else {
                        //показать тоаст
                    }
                }
            }
            composable(route = Screen.FavouritesScreen.route) { backStackEntry ->
                FavouriteScreenContent()
            }
        }
    }
}