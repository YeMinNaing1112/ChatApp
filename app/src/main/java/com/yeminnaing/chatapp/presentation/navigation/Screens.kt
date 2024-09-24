package com.yeminnaing.chatapp.presentation.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class Screens {
    @Serializable
    data object RegisterScreen : Screens()
    @Serializable
    data object SignInScreen : Screens()
    @Serializable
    data class ChatScreen(val id:String) : Screens()
    @Serializable
    data object HomeScreen:Screens()
}