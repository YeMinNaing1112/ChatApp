package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.yeminnaing.chatapp.domain.responses.UserResponse
import javax.inject.Inject

class ProfileRealTimeDataBaseImpl @Inject
constructor(
    private val realTimeDataBase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
):ProfileFireBaseApi {
    override fun getUserProfile(
        onSuccess: (user: UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        val userId= firebaseAuth.currentUser?.uid ?:""
       realTimeDataBase.child("users").child(userId).addValueEventListener(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val user= snapshot.getValue(UserResponse::class.java)
               if (user!=null){
                   onSuccess(user)
               }
           }

           override fun onCancelled(error: DatabaseError) {
              onFailure(error.message)
           }

       })
    }
}