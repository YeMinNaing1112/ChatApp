package com.yeminnaing.chatapp.presentation.screens.homeScreen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.yeminnaing.chatapp.domain.responses.ChatResponse
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenVm = hiltViewModel()
    val chatsStates by viewModel.getChatsStates.collectAsState()
    val getLastMessageStates by viewModel.getLastMessage.collectAsState()
    val context= LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }
    HomeScreenDesign(chatsStates, getLastMessageStates, addChannel = {
//        viewModel.addChannel(it)
    }, navigateToChatScreen =
    viewModel::navigateToChatScreen,
        navigateToSearchScreen =
        viewModel::navigateToSearchScreen,
        getLastMessage = {
            viewModel.upDateChatWithLastMessage(it)
        }


    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenDesign(
    chatsStates: HomeScreenVm.GetChatsStates,
    getLastMessageStates: HomeScreenVm.GetLastMessage,
    addChannel: (String) -> Unit,
    getLastMessage: (String) -> Unit,
    navigateToChatScreen: (String) -> Unit,
    navigateToSearchScreen: () -> Unit,
) {
    val context = LocalContext.current
    val addChannelDialog = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        modifier = Modifier.background(AppTheme.colorScheme.primary),
        floatingActionButton = {
            FloatingActionButton(onClick = { addChannelDialog.value = true }) {
                Text(text = "+")
            }

        },
    ) {
        Column(
            modifier = Modifier
                .background(AppTheme.colorScheme.primary)
                .padding(it)
                .fillMaxSize()

        ) {

            TopAppBar(navigateToSearchScreen = { navigateToSearchScreen() })
            when (chatsStates) {
                is HomeScreenVm.GetChatsStates.Empty -> {

                }

                is HomeScreenVm.GetChatsStates.Error -> {

                }

                is HomeScreenVm.GetChatsStates.Loading -> {

                }

                is HomeScreenVm.GetChatsStates.Success -> {
                    LazyColumn {
                        items(chatsStates.data) { chat ->
                            getLastMessage(chat.chatId)
                            Row(modifier = Modifier.clickable {
                                navigateToChatScreen(chat.chatId)

                            }) {
                                findTargetUser(chat.participants)?.let { targetUser ->
                                    Text(color = AppTheme.colorScheme.secondary,
                                        text = targetUser.firstOrNull().toString(),
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(Color.Red.copy(alpha = 0.3f))
                                            .padding(16.dp),
                                        style = TextStyle(
                                            fontSize = 16.sp, color = Color.Black
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    findTargetUser(chat.participants)?.let { targetUser ->
                                        Text(color = AppTheme.colorScheme.secondary,
                                            text = targetUser,
                                        )
                                    }
                                    when (getLastMessageStates) {
                                        is HomeScreenVm.GetLastMessage.Success -> {
                                            Text( color = AppTheme.colorScheme.secondary,
                                                text = getLastMessageStates.message.text,
                                            )
                                        }

                                        else -> {}
                                    }

                                }


                            }
                        }
                    }
                }

                else -> {}
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


fun findTargetUser(participants: Map<String, Boolean>): String? {
    val currentUserName = Firebase.auth.currentUser?.displayName
    val participant1 = participants.keys.elementAtOrNull(0)
    val participant2 = participants.keys.elementAtOrNull(1)
    return if (participant1 != currentUserName) participant1 else participant2
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

@Composable
fun TopAppBar(modifier: Modifier = Modifier, navigateToSearchScreen: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "Menu",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(45.dp)
                , tint = AppTheme.colorScheme.secondary
        )

        Icon(imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier
                .padding(end = 16.dp)
                .size(45.dp)
                .clickable {
                    navigateToSearchScreen()
                } , tint = AppTheme.colorScheme.secondary)
    }
}


@Preview
@Composable
private fun HomeScreenDesignPrev() {
    HomeScreenDesign(chatsStates = HomeScreenVm.GetChatsStates.Success(
        listOf(
            ChatResponse(
                chatId = "1", mapOf(
                    "User1" to true, "User2" to true
                )
            )
        )
    ),
        HomeScreenVm.GetLastMessage.Empty,
        addChannel = {},
        navigateToChatScreen = {},
        getLastMessage = {},
        navigateToSearchScreen = {})
}

@Preview
@Composable
private fun AddChannelDialogPrev() {
    AddChannelDialog(onAddChannel = {})
}