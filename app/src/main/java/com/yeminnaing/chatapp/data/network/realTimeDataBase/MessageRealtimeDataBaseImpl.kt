package com.yeminnaing.chatapp.data.network.realTimeDataBase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yeminnaing.chatapp.data.network.ApiServices
import com.yeminnaing.chatapp.data.responses.FcmRequestBody
import com.yeminnaing.chatapp.data.responses.FcmResponse
import com.yeminnaing.chatapp.data.responses.Message
import com.yeminnaing.chatapp.data.responses.Notification
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class MessageRealtimeDataBaseImpl @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseMessaging: FirebaseMessaging,
    private val apiServices: ApiServices,
) : MessageFireBaseApi {
    override fun sendMessage(chatId: String, message: String, context: Context) {
        val messageText = MessageResponse(
            id = firebaseDatabase.push().key ?: UUID.randomUUID().toString(),
            senderId = firebaseAuth.currentUser?.uid ?: "",
            text = message,
            senderName = firebaseAuth.currentUser?.displayName ?: "",
//            senderImage = null,
//            imageUrl = null
        )
        firebaseDatabase.child("messages").child(chatId).push().setValue(messageText)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    postNotificationToUsers(
                        chatId = chatId,
                        firebaseAuth.currentUser?.displayName ?: "",
                        messageContent = message,
                        context = context
                    )
                }
            }
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
                    snapshot.children.forEach { messageData ->
                        val message = messageData.getValue(MessageResponse::class.java)
                        message?.let { messageList.add(it) }
                    }
                    onSuccess(messageList)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
//        subscribeForNotification(chatId = chatId)

    }


    private fun subscribeForNotification(chatId: String) {
        FirebaseMessaging.getInstance().subscribeToTopic("group_$chatId")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("ChatViewModel", "Subscribed to topic: group_$chatId")
                } else {
                    Log.d("ChatViewModel", "Failed to subscribe to topic: group_$chatId")
                    // Handle failure
                }
            }
    }

    private fun postNotificationToUsers(
        chatId: String,
        senderName: String,
        messageContent: String,
        context: Context,
    ) {

//        val fcmUrl = "https://fcm.googleapis.com/v1/projects/chatapp-60ab0/messages:send"
//        val jsonBody = JSONObject().apply {
//            put("message", JSONObject().apply {
//                put("topic", "group_$chatId")
//                put("notification", JSONObject().apply {
//                    put("title", "New message in $chatId")
//                    put("body", "$senderName: $messageContent")
//                })
//            })
//
//        }
//
//        val requestBody = jsonBody.toString()
//
//        val request = object : StringRequest(Method.POST, fcmUrl, Response.Listener {
//            Log.d("ChatViewModel", "Notification sent successfully")
//        }, Response.ErrorListener {
//            Log.e("ChatViewModel", "Failed to send notification")
//        }) {
//            override fun getBody(): ByteArray {
//                return requestBody.toByteArray()
//            }
//
//            override fun getHeaders(): MutableMap<String, String> {
//                val headers = HashMap<String, String>()
//                headers["Authorization"] = "Bearer ${getAccessToken(context)}"
//                headers["Content-Type"] = "application/json"
//                return headers
//            }
//        }
//        val queue = Volley.newRequestQueue(context)
//        queue.add(request)
//        val notification = Notification(
//            title = senderName,
//            body = messageContent
//        )
//        val message = Message(
//            topic = "group $chatId",
//            notification = notification
//        )
//        val fcmRequestBody = FcmRequestBody(message)
//        apiServices.sendNotification(fcmRequestBody).enqueue(object : Callback<FcmResponse>{
//            override fun onResponse(call: Call<FcmResponse>, response: Response<FcmResponse>) {
//                if (response.isSuccessful) {
//                    Log.d("Notification", "Notification sent successfully: ${response.body()?.name}")
//                } else {
//                    Log.e("Notification", "Failed to send notification: ${response.code()} ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<FcmResponse>, t: Throwable) {
//                Log.e("Notification", "Error sending notification: ${t.message}")
//            }
//
//        })

    }


    override fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
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
            })
    }


}