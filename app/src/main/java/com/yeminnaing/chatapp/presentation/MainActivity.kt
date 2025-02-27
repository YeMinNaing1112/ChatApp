package com.yeminnaing.chatapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.yeminnaing.chatapp.presentation.navigation.ChatScreensNavGraph
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import com.yeminnaing.chatapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                ChatScreensNavGraph(navigator)
            }
        }
    }
}

