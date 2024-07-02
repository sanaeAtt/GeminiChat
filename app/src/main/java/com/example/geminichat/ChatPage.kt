package com.example.geminichat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geminichat.ui.theme.DarckBleu
import com.example.geminichat.ui.theme.PurpleGray

@Composable
fun chatPage(viewModel: ChatViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        AppHeader(onNewDiscussionClick = { viewModel.resetChat() })
        Column(
            modifier = Modifier.weight(1f)
        ) {
            MessageList(
                modifier = Modifier.weight(1f), messageList = viewModel.messageList
            )
            MessageInput(onMessageSend = {
                viewModel.sendMessage(question = it)
            })
        }
    }
}

@Composable
fun AppHeader(onNewDiscussionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Bot Helper",
            color = Color.White,
            fontSize = 22.sp
        )
        IconButton(onClick = onNewDiscussionClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_new_discussion),
                contentDescription = "Start New Discussion",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) =
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.ic_question_answer),
                contentDescription = "icon",
                tint = DarckBleu
            )
            Text(text = "Ask something ...", fontSize = 22.sp)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .background(Color.Transparent),
            reverseLayout = true
        ) {
            items(messageList.reversed()) { message ->
                if (message.message == "Typing...") {
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    MessageRow(messageModel = message)
                }
            }
        }
    }

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.rool == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(
                        if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                    )
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(18f))
                    .background(if (isModel) DarckBleu else PurpleGray)
                    .padding(16.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }
        }
    }
}
@Composable
fun MessageInput(onMessageSend: (String) -> Unit, modifier: Modifier = Modifier) {
    var message by remember {
        mutableStateOf("")
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {
                message = it
            },
        )
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = "" // clear the box
            }
        }) {
            Icon(
                imageVector = Icons.Default.Send, contentDescription = "Send"
            )
        }
    }
}
