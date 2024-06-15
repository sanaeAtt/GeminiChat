package com.example.geminichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.geminichat.ui.theme.GeminiChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        val ChatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        setContent {
            GeminiChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    chatPage(modifier = Modifier.padding(innerPadding),ChatViewModel)
                }
            }
        }
    }
}
