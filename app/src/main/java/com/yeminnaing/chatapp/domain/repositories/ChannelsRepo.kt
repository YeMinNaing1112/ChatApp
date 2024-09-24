package com.yeminnaing.chatapp.domain.repositories

import com.yeminnaing.chatapp.domain.responses.ChannelResponse

interface ChannelsRepo {
    fun getChannels(onSuccess: (channels:List<ChannelResponse>) -> Unit, onFailure: (String) -> Unit)
    fun addChannels(name:String)
}