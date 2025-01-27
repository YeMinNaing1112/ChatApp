package com.yeminnaing.chatapp.data.repositories

import com.yeminnaing.chatapp.data.network.auth.AuthManager
import com.yeminnaing.chatapp.data.network.auth.AuthManagerImpl
import com.yeminnaing.chatapp.domain.repositories.AuthRepo
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val mAuthManagerImpl: AuthManagerImpl,
) : AuthRepo {
    override fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        mAuthManagerImpl.login(email, password, onSuccess, onFailure)
    }

    override fun register(
        email: String,
        password: String,
        userName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        mAuthManagerImpl.register(email, password, userName, onSuccess, onFailure)
    }

    override fun checkAuthStatus(): Boolean {
        return mAuthManagerImpl.checkAuthState()
    }

    override fun signOut() {
        mAuthManagerImpl.signOut()
    }
}