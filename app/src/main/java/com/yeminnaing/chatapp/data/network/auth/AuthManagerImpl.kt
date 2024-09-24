package com.yeminnaing.chatapp.data.network.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val mFirebaseAuth: FirebaseAuth,
) : AuthManager {
    override fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful && it.isComplete) {
                    onSuccess()
                } else {
                    onFailure(it.exception?.message ?: "Please Check The Internet Connection")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful && it.isComplete) {
                    it.result.user?.updateProfile(
                        com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                    )
                    onSuccess()
                } else {
                    onFailure(it.exception?.message ?: "Please Check The Internet Connection")
                }
            }
    }

    override fun checkAuthState(): Boolean {
        return mFirebaseAuth.currentUser != null
    }

    override fun signOut() {
        mFirebaseAuth.signOut()
    }
}