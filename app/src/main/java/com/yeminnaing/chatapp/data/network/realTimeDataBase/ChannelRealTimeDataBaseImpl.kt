package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yeminnaing.chatapp.domain.responses.ChannelResponse
import javax.inject.Inject

class ChannelRealTimeDataBaseImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
) : ChannelFireBaseApi {
    override fun getChannels(
        onSuccess: (channels: List<ChannelResponse>) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        firebaseDatabase.getReference("channel").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val channelList = mutableListOf<ChannelResponse>()
                snapshot.children.forEach { data ->
                    val channel = ChannelResponse(id = data.key!!, name = data.value.toString())
                    channelList.add(channel)
                }
                onSuccess(channelList)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }
        })
    }

        override fun addChannels(name: String) {
       val key= firebaseDatabase.getReference("channel").push().key

        firebaseDatabase.getReference("channel").child(key!!).setValue(name)
    }
}