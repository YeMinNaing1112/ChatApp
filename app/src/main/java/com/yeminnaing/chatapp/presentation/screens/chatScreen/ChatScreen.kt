package com.yeminnaing.chatapp.presentation.screens.chatScreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.yeminnaing.chatapp.domain.responses.Message
import com.yeminnaing.chatapp.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(chatId: String) {
    val viewModel: ChatScreenVm = hiltViewModel()
    val messageStates by viewModel.getMessageStates.collectAsState()
    val context = LocalContext.current

    val id = chatId.replace("\"", "")
    BackHandler {
        viewModel.navigateBackToHome()
    }



    Scaffold {
        Column(
            modifier = Modifier
                .background(AppTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(it)
                .fillMaxSize()


        ) {

            LaunchedEffect(viewModel) {
                viewModel.listenForMessage(chatId = id)
            }
            when (messageStates) {
                is ChatScreenVm.GetMessageStates.Empty -> {

                }

                is ChatScreenVm.GetMessageStates.Error -> {
                    Toast.makeText(context, "It's Error", Toast.LENGTH_SHORT).show()

                }

                is ChatScreenVm.GetMessageStates.Loading -> {

                }

                is ChatScreenVm.GetMessageStates.Success -> {
                    ChatScreenDesign(
                        messages = (messageStates as ChatScreenVm.GetMessageStates.Success).data,
                        onSendMessage = { message ->

                            viewModel.sendMessage(id, message)

                        })
                }

            }


        }
    }
}

@Composable
fun ChatScreenDesign(
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
) {
    val hideKeyboardController = LocalSoftwareKeyboardController.current

    val msg = remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.padding(bottom = 52.dp),
        ){
            items(messages) { message ->
                ChatList(message = message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = msg.value,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type a message") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    hideKeyboardController?.hide()
                })
            )
            IconButton(onClick = {
                onSendMessage(msg.value)
                msg.value = ""
            }) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = "send")
            }
        }
    }
}


@Composable
fun ChatList(modifier: Modifier = Modifier, message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        AppTheme.colorScheme.onPrimary
    } else {
        AppTheme.colorScheme.background
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)

    ) {
        val alignment = if (!isCurrentUser) Alignment.CenterStart else Alignment.CenterEnd
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                .align(alignment)
        ) {
            Text(
                text = message.text, color = Color.Black, modifier = Modifier.padding(8.dp)
            )
        }

    }
}


@Preview
@Composable
private fun ChatListPrev() {
    ChatList(
        message = Message(
            id = "",
            senderId = "",
            text = "",
            senderName = "",
        )
    )
}

@Preview
@Composable
private fun ChatScreenDesignPrev() {
    ChatScreenDesign(
        messages = listOf<Message>(
            Message(
                id = "",
                senderId = "",
                text = "",
                senderName = "",
            )

        ),
        onSendMessage = {}
    )
}