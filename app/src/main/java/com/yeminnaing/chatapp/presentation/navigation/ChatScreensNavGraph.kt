package com.yeminnaing.chatapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yeminnaing.chatapp.presentation.authScreen.RegisterScreen
import com.yeminnaing.chatapp.presentation.authScreen.SignInScreen
import com.yeminnaing.chatapp.presentation.chatScreen.ChatScreen
import com.yeminnaing.chatapp.presentation.homeScreen.HomeScreen

@Composable
fun ChatScreensNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SignInScreen
    ) {
        composable<Screens.SignInScreen> {
            SignInScreen(navController)
        }
        composable<Screens.RegisterScreen> {
            RegisterScreen(navController)
        }
        composable<Screens.ChatScreen> { backStackEntry->
            val chat = backStackEntry.toRoute<Screens.ChatScreen>()
            ChatScreen(chat.id)
        }
        composable<Screens.HomeScreen> {
            HomeScreen(navController)
        }
    }
}


