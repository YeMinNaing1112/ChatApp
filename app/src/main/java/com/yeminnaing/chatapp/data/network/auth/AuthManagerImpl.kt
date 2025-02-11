package com.yeminnaing.chatapp.data.network.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.yeminnaing.chatapp.domain.responses.UserResponse
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val mFirebaseAuth: FirebaseAuth,
    private val mFirebaseDatabase: DatabaseReference
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
        address:String,
        bio:String,
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
                    val userId=mFirebaseAuth.currentUser?.uid ?: ""
                    val user = UserResponse(
                        userId =userId ,
                        email=email,
                        username = name,
                        address=address,
                        bio=bio
                    )
                    mFirebaseDatabase.child("users").child(userId).setValue(user)
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