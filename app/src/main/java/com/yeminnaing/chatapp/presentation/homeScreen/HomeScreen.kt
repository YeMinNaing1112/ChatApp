package com.yeminnaing.chatapp.presentation.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yeminnaing.chatapp.domain.responses.ChannelResponse
import com.yeminnaing.chatapp.presentation.navigation.Screens

@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel: HomeScreenVm = hiltViewModel()
    val channels by viewModel.getChannelsState.collectAsState()
    HomeScreenDesign(channels, addChannel = {  viewModel.addChannel(it)},
        navigateToChatScreen = {id->
              navController.navigate(Screens.ChatScreen(id)){
                  popUpTo(navController.graph.findStartDestination().id){
                      inclusive=true
                      saveState=false
                  }
                  launchSingleTop=true
              }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenDesign(channels: HomeScreenVm.GetChannelsState,addChannel:(String)->Unit,navigateToChatScreen:(String)->Unit) {
    val addChannelDialog = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { addChannelDialog.value = true }) {
            Text(text = "+")
        }

    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            when (channels) {
                is HomeScreenVm.GetChannelsState.Empty -> {

                }

                is HomeScreenVm.GetChannelsState.Error -> {

                }

                is HomeScreenVm.GetChannelsState.Loading -> {

                }

                is HomeScreenVm.GetChannelsState.Success -> {
                    LazyColumn {
                        items(channels.data) { channel ->
                            Column {
                                Text(text = channel.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.Red.copy(alpha = 0.3f))
                                        .clickable {
                                           navigateToChatScreen(channel.id)
                                        }
                                        .padding(16.dp))
                            }
                        }
                    }
                }
            }


        }
    }

    if (addChannelDialog.value) {
        Dialog(onDismissRequest = { addChannelDialog.value = false }) {
            AddChannelDialog {
                addChannel(it)
                addChannelDialog.value = false
            }
        }
    }
}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Channel")
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(value = channelName.value, onValueChange = {
            channelName.value = it
        }, label = { Text(text = "Channel Name") }, singleLine = true)
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = { onAddChannel(channelName.value) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add")
        }
    }
}

@Preview
@Composable
private fun HomeScreenDesignPrev() {
    HomeScreenDesign(
        channels = HomeScreenVm.GetChannelsState.Success(
            listOf(
                ChannelResponse(id = "1", name = "Name")
            )
        ), addChannel = {}, navigateToChatScreen = {}
    )
}

@Preview
@Composable
private fun AddChannelDialogPrev() {
    AddChannelDialog(onAddChannel = {})
}