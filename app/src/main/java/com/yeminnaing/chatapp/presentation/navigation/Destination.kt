package com.yeminnaing.chatapp.presentation.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class Destination {
    @Serializable
    data object AuthGraph:Destination()
    @Serializable
    data object HomeGraph:Destination()
    @Serializable
    data object RegisterScreen : Destination()
    @Serializable
    data object SignInScreen : Destination()
    @Serializable
    data class ChatScreen(val id:String) : Destination()
    @Serializable
    data object HomeScreen:Destination()
    @Serializable
    data object SearchScreen:Destination()
}