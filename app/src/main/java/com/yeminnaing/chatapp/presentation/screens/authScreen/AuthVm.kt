package com.yeminnaing.chatapp.presentation.screens.authScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeminnaing.chatapp.domain.repositories.AuthRepo
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVm @Inject constructor(
    private val mAuthRepoImpl: AuthRepo,
    private val navigator: Navigator,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthStates>(AuthStates.Empty)
    val authStates = _authState.asStateFlow()

    init {
        checkAuthState()
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

    fun navigateToSignInScreen() {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.SignInScreen,
                navOption = {
                    popUpTo(Destination.RegisterScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
    }

    fun navigateToRegisterScreen() {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.RegisterScreen,
                navOption = {
                    popUpTo(Destination.SignInScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
    }

    private fun checkAuthState() {
        if (mAuthRepoImpl.checkAuthStatus()) {
            _authState.value = AuthStates.Authenticated
        } else {
            _authState.value = AuthStates.UnAuthenticated

        }
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthStates.Error("Email or Password can't be empty")
        }
        _authState.value = AuthStates.Loading
        mAuthRepoImpl.login(
            email = email,
            password = password,
            onSuccess = {
                _authState.value = AuthStates.Authenticated
            }, onFailure = {
                _authState.value = AuthStates.Error(it)
            }
        )
    }

    fun register(name: String, email: String, password: String,address:String,bio:String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthStates.Error("Email or password can't be empty")
            return
        }
        mAuthRepoImpl.register(
            email = email,
            password = password,
            userName = name,
            address = address,
            bio = bio,
            onSuccess = {
                _authState.value = AuthStates.Authenticated
            }, onFailure = {
                _authState.value = AuthStates.Error(it)
            }
        )
    }

    fun signOut() {
        mAuthRepoImpl.signOut()
        _authState.value = AuthStates.UnAuthenticated
    }

    sealed class AuthStates() {
        data object Authenticated : AuthStates()
        data object UnAuthenticated : AuthStates()
        data object Loading : AuthStates()
        data object Empty : AuthStates()
        data class Error(val message: String) : AuthStates()
    }
}