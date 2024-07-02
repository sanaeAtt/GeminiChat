package com.example.geminichat

import ChatViewModel
import PreviousDiscussion
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.geminichat.ui.theme.GeminiChatTheme
import com.example.geminichat.ui.theme.PurpleGrey40
import com.example.geminichat.ui.theme.bleuApp
import com.example.geminichat.ui.theme.darkbleu
import com.example.geminichat.ui.theme.whiteDove
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            GeminiChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(whiteDove)
                            .padding(innerPadding)
                    ) {
                        SlidingMenuContainer(chatViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SlidingMenuContainer(viewModel: ChatViewModel, modifier: Modifier = Modifier) {
    var offsetX by remember { mutableStateOf(-250f) }  // Start hidden off-screen

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    val newValue = offsetX + dragAmount
                    offsetX = newValue.coerceIn(-250f, 0f)  // Limit slider position
                }
            }
    ) {
        // Main content
        chatPage(viewModel = viewModel)

        // Sliding menu
        Column(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .offset(x = offsetX.dp)
                .background(whiteDove)
                .padding(top = 16.dp)  // Add top padding
        ) {
            Text(
                text = stringResource(id = R.string.recent),
                fontSize = 18.sp,
                color = bleuApp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DisplayDiscussions(viewModel.previousDiscussions, viewModel)
            Spacer(modifier = Modifier.weight(1f)) // Spacer to push the button to the bottom
            Button(
                onClick = { viewModel.previousDiscussions.clear() },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = bleuApp),

            ) {
                Text(text = stringResource(id = R.string.all), color = whiteDove)
            }
        }
    }
}

@Composable
fun DisplayDiscussions(discussions: List<PreviousDiscussion>, viewModel: ChatViewModel) {
    val now = LocalDateTime.now()

    val todayDiscussions = discussions.filter {
        it.timestamp.toLocalDate().isEqual(LocalDate.now())
    }
    val yesterdayDiscussions = discussions.filter {
        it.timestamp.toLocalDate().isEqual(LocalDate.now().minusDays(1))
    }
    val last7DaysDiscussions = discussions.filter {
        it.timestamp.isAfter(now.minusDays(7)) && it.timestamp.isBefore(now.minusDays(1))
    }
    val last30DaysDiscussions = discussions.filter {
        it.timestamp.isAfter(now.minusDays(30)) && it.timestamp.isBefore(now.minusDays(7))
    }

    LazyColumn {
        if (todayDiscussions.isNotEmpty()) {
            item {
                DiscussionSection(stringResource(id = R.string.auj), todayDiscussions, viewModel)
            }
        }
        if (yesterdayDiscussions.isNotEmpty()) {
            item {
                DiscussionSection(stringResource(id = R.string.hier), yesterdayDiscussions, viewModel)
            }
        }
        if (last7DaysDiscussions.isNotEmpty()) {
            item {
                DiscussionSection(stringResource(id = R.string.seven), last7DaysDiscussions, viewModel)
            }
        }
        if (last30DaysDiscussions.isNotEmpty()) {
            item {
                DiscussionSection(stringResource(id = R.string.THRT), last30DaysDiscussions, viewModel)
            }
        }
    }
}

@Composable
fun DiscussionSection(title: String, discussions: List<PreviousDiscussion>, viewModel: ChatViewModel) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = darkbleu,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        discussions.forEach { discussion ->
            val firstMessage = discussion.messages.firstOrNull()?.message ?: stringResource(id = R.string.ampty)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(color = PurpleGrey40)
                    .clickable {
                        viewModel.loadPreviousDiscussion(discussion)
                    },
                elevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = firstMessage.take(30) + if (firstMessage.length > 30) "..." else "",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal,
                            color = bleuApp
                        )
                        Text(
                            text = discussion.timestamp.toLocalDate().toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = { viewModel.removeDiscussion(discussion) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete),
                            tint = bleuApp
                        )
                    }
                }
            }
        }
    }
}
