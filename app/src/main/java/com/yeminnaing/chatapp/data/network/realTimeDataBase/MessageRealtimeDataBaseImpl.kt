package com.yeminnaing.chatapp.data.network.realTimeDataBase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.yeminnaing.chatapp.domain.responses.Message
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import com.yeminnaing.chatapp.domain.responses.MetaData
import java.util.UUID
import javax.inject.Inject

class MessageRealtimeDataBaseImpl @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
) : MessageFireBaseApi {
    override fun sendMessage(chatId: String, message: String) {
        val messageId = firebaseDatabase.push().key ?: UUID.randomUUID().toString()
        val messageText = Message(
            id = messageId,
            senderId = firebaseAuth.currentUser?.uid ?: "",
            text = message,
            senderName = firebaseAuth.currentUser?.displayName ?: "",
            timeStamp = System.currentTimeMillis()
        )

        val messageRef = firebaseDatabase.child("messages").child(chatId).child("messageList").child(messageId)

        // Save the new message
        messageRef.setValue(messageText)
            .addOnSuccessListener {
                // Update chat metadata (last message and timestamp)
                val updatedResponse = MetaData(
                    chatId = chatId,
                    lastMessage = messageText.text,
                    timeStamp = messageText.timeStamp
                )
                firebaseDatabase.child("messages").child(chatId).child("metadata").setValue(updatedResponse)
            }
            .addOnFailureListener {
                Log.e("Firebase", "Failed to send message: ${it.message}")
            }

    }


    override fun listenForMessage(
        onSuccess: (messages: List<Message>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
        firebaseDatabase.child("messages").child(chatId).child("messageList")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val messageResponse = snapshot.getValue(MessageResponse::class.java)
//                    if (messageResponse != null) {
//                        onSuccess(messageResponse.messageList)
//                    } else {
//                        onFailure("No messages found")
//                    }
//                override fun onDataChange(snapshot: DataSnapshot) {
                        val messageList = mutableListOf<Message>()
                       snapshot.children.forEach{messagesnapshot->
                           val message=messagesnapshot.getValue(Message::class.java)
                           message?.let { messageList.add(it) }
                       }

                    if (messageList.isNotEmpty()) {
                        onSuccess(messageList)
                    } else {
                        onFailure("No messages found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure("error.message")
                }
            })
    }


    override fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
            firebaseDatabase.child("messages").child(chatId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messageListSnapshot = snapshot.child("messageList")
                        val messageList = mutableListOf<Message>()

                        for (child in messageListSnapshot.children) {
                            val message = child.getValue(Message::class.java)
                            message?.let { messageList.add(it) }
                        }

                        val metaData = snapshot.child("metadata").getValue(MetaData::class.java)?: MetaData()
                        val messageResponse = MessageResponse(
                           metaData=metaData,
                            messageList = messageList
                        )
                        onSuccess(messageResponse)
                    }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            })
    }

    override fun createMessage(chatId: String) {
        val metaData = MetaData(
            chatId = chatId,
            lastMessage = "",
        )
        val messageList : List<Message> = listOf(
            Message(
                id = "",
                senderId = "",
                senderName = "",
                text = ""
            )
        )

        val messageResponse = MessageResponse(
            metaData= metaData,
            messageList=messageList
        )
        firebaseDatabase.child("messages").child(chatId).setValue(messageResponse)
    }


}