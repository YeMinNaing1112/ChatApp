package com.yeminnaing.chatapp.data.network.auth

interface AuthManager {
    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun register(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    )

    fun checkAuthState(): Boolean
    fun signOut()
}