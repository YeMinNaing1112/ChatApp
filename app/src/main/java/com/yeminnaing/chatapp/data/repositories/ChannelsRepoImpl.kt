package com.yeminnaing.chatapp.data.repositories

import com.yeminnaing.chatapp.data.network.realTimeDataBase.ChannelRealTimeDataBaseImpl
import com.yeminnaing.chatapp.domain.repositories.ChannelsRepo
import com.yeminnaing.chatapp.domain.responses.ChannelResponse
import javax.inject.Inject

class ChannelsRepoImpl @Inject constructor(
private val dataBaseImpl: ChannelRealTimeDataBaseImpl
):ChannelsRepo {
    override fun getChannels(
        onSuccess: (channels: List<ChannelResponse>) -> Unit,
        onFailure: (String) -> Unit,
    ) {
       dataBaseImpl.getChannels(onSuccess,onFailure)
    }

    override fun addChannels(name: String) {
        dataBaseImpl.addChannels(name)
    }
}