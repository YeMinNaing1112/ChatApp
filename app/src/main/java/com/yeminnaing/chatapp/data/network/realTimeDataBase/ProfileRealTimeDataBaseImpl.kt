package com.yeminnaing.chatapp.data.network.realTimeDataBase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.yeminnaing.chatapp.domain.responses.UserResponse
import java.util.UUID
import javax.inject.Inject

class ProfileRealTimeDataBaseImpl @Inject
constructor(
    private val realTimeDataBase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: StorageReference,
) : ProfileFireBaseApi {
    override fun getUserProfile(
        onSuccess: (user: UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        val userId = firebaseAuth.currentUser?.uid ?: ""
        realTimeDataBase.child("users").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserResponse::class.java)
                    if (user != null) {
                        onSuccess(user)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }

            })
    }

    override fun editUserProfile(
        email: String,
        name: String,
        address: String,
        bio: String,
        image:String
    ) {
        val userId = firebaseAuth.currentUser?.uid ?: ""
        val user = UserResponse(
            userId = userId,
            email = email,
            username = name,
            address = address,
            bio = bio,
            image=image
        )
        realTimeDataBase.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("EditProfile", "editUserProfile: ")
            }
            .addOnFailureListener {
                Log.d("EditProfile", "editUserProfile: $it ")
            }
    }

    override fun uploadImage(imageUri: Uri, user: UserResponse, context: Context) {
        val imageRef = firebaseStorage.child("images/${UUID.randomUUID()}")
        val byteArray: ByteArray? = context.contentResolver
            .openInputStream(imageUri)?.use {
                it.readBytes()
            }
        val upLoadTask = byteArray?.let { imageRef.putBytes(it) }
        upLoadTask?.addOnFailureListener { }
            ?.addOnSuccessListener {

            }

        val urlTask = upLoadTask?.continueWithTask {
            return@continueWithTask imageRef.downloadUrl
        }?.addOnCompleteListener { task ->
            val imageUrl = task.result.toString()
            editUserProfile(email = user.email?:"", name = user.username, address = user.address, bio = user.bio,image=imageUrl)

        }


    }
}