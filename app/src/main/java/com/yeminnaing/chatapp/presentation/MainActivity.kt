package com.yeminnaing.chatapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yeminnaing.chatapp.presentation.navigation.ChatScreensNavGraph
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import com.yeminnaing.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var navigator: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ChatAppTheme {

                ChatScreensNavGraph(navigator)
            }
        }
    }
}

