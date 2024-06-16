package com.example.geminichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.geminichat.ui.theme.GeminiChatTheme
import com.example.geminichat.ui.theme.whiteDove

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            GeminiChatTheme {
                // Utilisation de Scaffold pour structurer l'interface utilisateur
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Box pour superposer l'image et le contenu de chatPage
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(whiteDove),

                        ) {

                        // Appel à chatPage pour afficher le contenu de la page de chat
                        chatPage(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = chatViewModel
                        )
                    }
                }
            }
        }
    }
}