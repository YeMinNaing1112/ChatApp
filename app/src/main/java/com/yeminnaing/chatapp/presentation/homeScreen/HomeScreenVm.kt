package com.yeminnaing.chatapp.presentation.homeScreen

import androidx.lifecycle.ViewModel
import com.yeminnaing.chatapp.data.repositories.ChannelsRepoImpl
import com.yeminnaing.chatapp.domain.responses.ChannelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenVm @Inject constructor(
    private val mChannelsRepoImpl: ChannelsRepoImpl,
) : ViewModel() {
    private val _getChannelsState = MutableStateFlow<GetChannelsState>(GetChannelsState.Empty)
    val getChannelsState = _getChannelsState.asStateFlow()

    init {
        getChannel()
    }

    private fun getChannel() {
        _getChannelsState.value = GetChannelsState.Loading
        mChannelsRepoImpl.getChannels(
            onSuccess = {
                _getChannelsState.value = GetChannelsState.Success(it)
            },
            onFailure = {
                _getChannelsState.value = GetChannelsState.Error(it)
            }
        )
    }


    fun addChannel(name: String) {
        mChannelsRepoImpl.addChannels(name = name)
    }

    sealed interface GetChannelsState {
        data object Empty : GetChannelsState
        data object Loading : GetChannelsState
        data class Success(val data: List<ChannelResponse>) : GetChannelsState
        data class Error(val error: String) : GetChannelsState
    }

}