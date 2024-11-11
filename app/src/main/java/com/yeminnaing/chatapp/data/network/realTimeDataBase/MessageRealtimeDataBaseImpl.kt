package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import java.util.UUID
import javax.inject.Inject

class MessageRealtimeDataBaseImpl @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
) : MessageFireBaseApi {
    override fun sendMessage(chatId: String, message: String) {
        val messageText = MessageResponse(
           id = firebaseDatabase.push().key ?: UUID.randomUUID().toString(),
            senderId = firebaseAuth.currentUser?.uid ?: "",
            text = message,
            senderName = firebaseAuth.currentUser?.displayName ?: "",
//            senderImage = null,
//            imageUrl = null
        )
        firebaseDatabase.child("messages").child(chatId).push().setValue(messageText)
    }

    override fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
        firebaseDatabase.child("messages").child(chatId).orderByChild("timestamp")
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

    override fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
//        firebaseDatabase.child("messages").child(chatId).orderByChild("timeStamp").limitToLast(1)
//            .addValueEventListener(object: ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                      val lastMessage = snapshot.getValue(MessageResponse::class.java)
//                      lastMessage?.let { onSuccess(it) }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//            })
//    }
        firebaseDatabase.child("messages").child(chatId).orderByChild("timeStamp").limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val messageSnapshot = snapshot.children.firstOrNull()
                        val lastMessage = messageSnapshot?.getValue(MessageResponse::class.java)
                        if (lastMessage != null) {
                            onSuccess(lastMessage)
                        } else {
                            onFailure("Message parsing error")
                        }
                    } else {
                        onFailure("No messages found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            })}

}