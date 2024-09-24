package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.yeminnaing.chatapp.domain.responses.ChannelResponse


interface ChannelFireBaseApi {
    fun getChannels(onSuccess: (channels:List<ChannelResponse>) -> Unit, onFailure: (String) -> Unit)
    fun addChannels(name:String)


}