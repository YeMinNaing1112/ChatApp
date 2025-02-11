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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.yeminnaing.chatapp.domain.responses.ChatResponse
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun HomeScreen() {
    val viewModel: HomeScreenVm = hiltViewModel()
    val chatsStates by viewModel.getChatsStates.collectAsState()
    val getLastMessageStatesMap by viewModel.getLastMessageMap.collectAsState()
    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.finish()
    }

    HomeScreenDesign(chatsStates,
        getLastMessageStatesMap,
        addChannel = {
//        viewModel.addChannel(it)
        },
        navigateToChatScreen = viewModel::navigateToChatScreen,
        navigateToSearchScreen = viewModel::navigateToSearchScreen,
        getLastMessage = {
            viewModel.upDateChatWithLastMessage(it)
        }, navigateToProfileScreen = {
            viewModel.navigateToProfileScreen()
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenDesign(
    chatsStates: HomeScreenVm.GetChatsStates,
    getLastMessageStates: Map<String, HomeScreenVm.GetLastMessage>,
    addChannel: (String) -> Unit,
    getLastMessage: (String) -> Unit,
    navigateToChatScreen: (String) -> Unit,
    navigateToSearchScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
) {
    val addChannelDialog = remember {
        mutableStateOf(false)
    }
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

            TopAppBar(navigateToSearchScreen = {
                navigateToSearchScreen()
            }, navigateToProfileScreen = {
                navigateToProfileScreen()
            })
            when (chatsStates) {
                is HomeScreenVm.GetChatsStates.Empty -> {

                }

                is HomeScreenVm.GetChatsStates.Error -> {

                }

                is HomeScreenVm.GetChatsStates.Loading -> {

                }

                is HomeScreenVm.GetChatsStates.Success -> {
                    LaunchedEffect(Unit) {
                        chatsStates.data.forEach { chat ->
                            getLastMessage(chat.chatId)
                        }
                    }
                    LazyColumn {
                        val sortedChats = chatsStates.data.sortedByDescending { chat ->
                            when (val lastMessageState = getLastMessageStates[chat.chatId]) {
                                is HomeScreenVm.GetLastMessage.Success -> lastMessageState.message.metaData?.timeStamp
                                else -> Long.MIN_VALUE
                            }
                        }

                        items(sortedChats) { chat ->
                            val lastMessageState = getLastMessageStates[chat.chatId]
                            Row(modifier = Modifier.clickable {
                                navigateToChatScreen(chat.chatId)
                            }, verticalAlignment = Alignment.CenterVertically) {

                                findTargetUser(chat.participants)?.let { targetUser ->
                                    Text(
                                        color = AppTheme.colorScheme.secondary,
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
                                        Text(
                                            color = AppTheme.colorScheme.secondary,
                                            text = targetUser,
                                        )
                                    }



                                    when (lastMessageState) {
                                        is HomeScreenVm.GetLastMessage.Success -> {
                                            val lastMessage =
                                                lastMessageState.message.metaData?.lastMessage
                                            if (lastMessageState.message.messageList.last().senderId == Firebase.auth.currentUser?.uid) {
                                                Text(
                                                    color = AppTheme.colorScheme.secondary,
                                                    text = "You: $lastMessage"
                                                )
                                            } else {
                                                Text(
                                                    color = AppTheme.colorScheme.secondary,
                                                    text = lastMessage ?: "",
                                                )
                                            }
                                        }

                                        else -> {}
                                    }

                                }


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


fun findTargetUser(participants: Map<String, Boolean>): String? {
    val currentUserName = Firebase.auth.currentUser?.displayName
    val participant1 = participants.keys.elementAtOrNull(0)
    val participant2 = participants.keys.elementAtOrNull(1)
    val participants1Name = participant1?.substringBefore("_")
    val participants2Name = participant2?.substringBefore("_")
    return if (participants1Name != currentUserName) participants1Name else participants2Name
}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
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
fun TopAppBar(
    modifier: Modifier = Modifier,
    navigateToSearchScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp),
                tint = AppTheme.colorScheme.secondary
            )

            Text(
                text = "Chat Room",
                color = AppTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .clickable {
                        navigateToProfileScreen()
                    },
                tint = AppTheme.colorScheme.secondary
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.LightGray)
                .clickable {
                    navigateToSearchScreen()
                }

        ) {
            Text(
                text = "Search",
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)

            )

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
            )
        }


    }

}


@Preview
@Composable
private fun HomeScreenDesignPrev() {
    HomeScreenDesign(
        chatsStates = HomeScreenVm.GetChatsStates.Success(
            listOf(
                ChatResponse(
                    chatId = "1", mapOf(
                        "User1" to true, "User2" to true
                    )
                )
            )
        ),
        getLastMessageStates = mapOf(),
        addChannel = {},
        navigateToChatScreen = {},
        getLastMessage = {},
        navigateToSearchScreen = {},
        navigateToProfileScreen = {}
    )
}

@Preview
@Composable
private fun AddChannelDialogPrev() {
    AddChannelDialog(onAddChannel = {})
}

@Preview
@Composable
private fun TopAppBarPrev() {
    TopAppBar(
        navigateToProfileScreen = {},
        navigateToSearchScreen = {}
    )
}