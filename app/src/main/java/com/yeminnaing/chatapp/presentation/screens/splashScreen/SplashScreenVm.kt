package com.yeminnaing.chatapp.presentation.screens.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeminnaing.chatapp.domain.repositories.AuthRepo
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import com.yeminnaing.chatapp.presentation.screens.authScreen.AuthVm.AuthStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenVm @Inject constructor(
    private val authRepo: AuthRepo,
    private val navigator: Navigator
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthStates>(AuthStates.Empty)
    val authStates = _authState.asStateFlow()
    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        if (authRepo.checkAuthStatus()){
            _authState.value=AuthStates.Authenticated
        }else{
            _authState.value=AuthStates.UnAuthenticated
        }
    }
    fun login() {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.HomeGraph,
                navOption = {
                    popUpTo(Destination.AuthGraph) {
                        inclusive = true
                    }
                }
            )
        }
    }

    fun navigateToRegisterScreen() {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.RegisterScreen,
                navOption = {
                    popUpTo(Destination.SplashScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
    }

}