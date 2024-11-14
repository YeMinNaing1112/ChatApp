package com.yeminnaing.chatapp.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface Navigator {
    val startDestination: Destination
    val navigationAction: Flow<NavigationAction>

    suspend fun navigate(
        destination: Destination,
        navOption: NavOptionsBuilder.() -> Unit = {},
    )

    suspend fun navigateUp()
}

class DefaultNavigator(
    override val startDestination: Destination,
) : Navigator {
    private val _navigationAction = Channel<NavigationAction>()
    override val navigationAction = _navigationAction.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        navOption: NavOptionsBuilder.() -> Unit,
    ) {
        _navigationAction.send(
            NavigationAction.Navigate(
                destination = destination,
                navOptions = navOption
            )
        )
    }

    override suspend fun navigateUp() {
        _navigationAction.send(NavigationAction.NavigateUp)
    }

}
