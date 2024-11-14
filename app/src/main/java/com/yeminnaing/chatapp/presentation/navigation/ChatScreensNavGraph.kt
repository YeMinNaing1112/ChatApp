package com.yeminnaing.chatapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yeminnaing.chatapp.presentation.screens.authScreen.RegisterScreen
import com.yeminnaing.chatapp.presentation.screens.authScreen.SignInScreen
import com.yeminnaing.chatapp.presentation.screens.chatScreen.ChatScreen
import com.yeminnaing.chatapp.presentation.screens.homeScreen.HomeScreen
import com.yeminnaing.chatapp.presentation.screens.searchScreen.SearchScreen

@Composable
fun ChatScreensNavGraph(navigator: Navigator) {

    val navController = rememberNavController()

    ObserveAsEvent(flow = navigator.navigationAction) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(
                action.destination
            ) {
                action.navOptions
            }

            is NavigationAction.NavigateUp -> navController.navigateUp()
        }
    }
    NavHost(
        navController = navController,
        startDestination = navigator.startDestination
    ) {
        navigation<Destination.AuthGraph>(
            startDestination = Destination.SignInScreen
        ) {
            composable<Destination.SignInScreen> {
                SignInScreen()
            }
            composable<Destination.RegisterScreen> {
                RegisterScreen()
            }
        }

        navigation<Destination.HomeGraph>(
            startDestination = Destination.HomeScreen
        ) {
            composable<Destination.HomeScreen> {
                HomeScreen()
            }

            composable<Destination.ChatScreen> { backStackEntry ->
                val chat: Destination.ChatScreen = backStackEntry.toRoute()
                ChatScreen(chat.id)
            }

            composable<Destination.SearchScreen> {
                SearchScreen()
            }
        }
    }
}


