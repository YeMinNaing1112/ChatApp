package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.yeminnaing.chatapp.domain.responses.ChatResponse
import com.yeminnaing.chatapp.domain.responses.UserResponse
import javax.inject.Inject

class ChatsRealTimeDataBaseImpl @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
) : ChatsFireBaseApi {

    override fun getChats(
        onSuccess: (List<ChatResponse>) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        val userId = firebaseAuth.currentUser?.displayName ?: ""
        firebaseDatabase.child("chats").orderByChild("participants/$userId").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chats = mutableListOf<ChatResponse>()
                    snapshot.children.forEach { dataSnapshot ->
                        dataSnapshot.getValue(ChatResponse::class.java)?.let {
                            chats.add(it)
                        }
                    }
                    onSuccess(chats)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun createChat(
        chatId: String,
        targetUserId:String
    ) {
       firebaseDatabase.child("chats").child(chatId).addValueEventListener(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               if (!snapshot.exists()) {
                   val currentUserId = firebaseAuth.currentUser?.displayName ?: ""
                   val newChat = ChatResponse(
                       chatId = chatId,
                       participants = mapOf(currentUserId to true, targetUserId to true),
                       lastMessage = ""
                   )
                   firebaseDatabase.child("chats").child(chatId).setValue(newChat)
               }
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }

       })
    }
    override fun addLastMessageToChat(lastMessage: String,chatId:String) {
        firebaseDatabase.child("chats").child(chatId)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val chat= snapshot.getValue(ChatResponse::class.java)
                val editChat= chat?.let {
                    ChatResponse(
                        chatId=chatId,
                        lastMessage=lastMessage,
                        participants = it.participants,
                    )
                }
                firebaseDatabase.child("chats").child(chatId).setValue(editChat)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun findUserByEmail(
        email: String,
        onSuccess: (UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        firebaseDatabase.child("users").orderByChild("email").equalTo(email)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.children.firstOrNull()?.getValue(UserResponse::class.java)

                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onFailure("User not found")
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }


}