package com.example.geminichat

import ChatViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geminichat.ui.theme.DarckBleu
import com.example.geminichat.ui.theme.Vertpale
import com.example.geminichat.ui.theme.bleu
import com.example.geminichat.ui.theme.bleuApp

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
            MessageInput(
                onMessageSend = {
                    viewModel.sendMessage(question = it)

                }
            )
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
            text = stringResource(id = R.string.appname),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 22.sp
        )
        IconButton(onClick = onNewDiscussionClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_new_discussion),
                contentDescription = "Start New Discussion",
                tint = MaterialTheme.colorScheme.secondary,
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
                tint = MaterialTheme.colorScheme.onSecondary
            )
            Text(text = stringResource(id = R.string.ask), fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurface)
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
                    .background(if (isModel) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceTint)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onMessageSend: (String) -> Unit,modifier: Modifier = Modifier) {
    var message by remember {
        mutableStateOf("")
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 5.dp)
            .background(Color.Transparent)
            .pointerInput(Unit){
                detectTapGestures(onTap = {

                })
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {
                message = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // Change the border color when focused
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary // Change the border color when unfocused
            )
        )
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = stringResource(id = R.string.send),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
