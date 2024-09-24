package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import java.util.UUID
import javax.inject.Inject

class MessageRealtimeDataBaseImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
) : MessageFireBaseApi {
    override fun sendMessage(channelId: String, message: String) {
        val messageText = MessageResponse(
            id = firebaseDatabase.reference.push().key ?: UUID.randomUUID().toString(),
            senderId = firebaseAuth.currentUser?.uid ?: "",
            message = message,
            senderName = firebaseAuth.currentUser?.displayName ?: "",
            senderImage = null,
            imageUrl = null
        )
        firebaseDatabase.reference.child("message").child(channelId).push().setValue(messageText)
    }

    override fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        channelId: String,
    ) {
        firebaseDatabase.reference.child("message").child(channelId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = mutableListOf<MessageResponse>()
                    snapshot.children.forEach { messageData->
                      val message= messageData.getValue(MessageResponse::class.java)
                        message?.let { messageList.add(it) }
                    }
                    onSuccess(messageList)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}