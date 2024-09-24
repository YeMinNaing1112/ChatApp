package com.yeminnaing.chatapp.presentation.authScreen

import androidx.lifecycle.ViewModel
import com.yeminnaing.chatapp.data.repositories.AuthRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthVm @Inject constructor(
    private val mAuthRepoImpl: AuthRepoImpl,
) :ViewModel(){
    private val _authState = MutableStateFlow<AuthStates>(AuthStates.Empty)
    val authStates = _authState.asStateFlow()

    init {
        checkAuthState()
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
                  _authState.value=AuthStates.Authenticated
              }, onFailure = {
                _authState.value=AuthStates.Error(it)
            }
        )
    }

    fun register(name:String,email:String,password: String){
        if(email.isEmpty() || password.isEmpty() || name.isEmpty()){
            _authState.value = AuthStates.Error("Email or password can't be empty")
            return
        }
        mAuthRepoImpl.register(
            email=email,
            password=password,
            userName = name,
            onSuccess = {
                _authState.value=AuthStates.Authenticated
            }, onFailure = {
                _authState.value=AuthStates.Error(it)
            }
        )
    }
    fun signOut(){
       mAuthRepoImpl.signOut()
        _authState.value=AuthStates.UnAuthenticated
    }
    sealed class AuthStates() {
        data object Authenticated : AuthStates()
        data object UnAuthenticated : AuthStates()
        data object Loading : AuthStates()
        data object Empty : AuthStates()
        data class Error(val message: String) : AuthStates()
    }
}