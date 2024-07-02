package com.example.geminichat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatViewModel : ViewModel() {
    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val previousDiscussions by lazy {
        mutableStateListOf<PreviousDiscussion>()
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apiKey
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.rool) {
                            text(it.message)
                        }
                    }.toList()
                )
                messageList.add(MessageModel(question, "user"))
                val typingMessage = MessageModel("Typing...", "model")
                messageList.add(typingMessage)
                val response = chat.sendMessage(question)
                messageList.remove(typingMessage)
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e: Exception) {
                messageList.removeLast()
                messageList.add(MessageModel("Error: " + e.message.toString(), "model"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun resetChat() {
        if (messageList.isNotEmpty()) {
            val discussion = PreviousDiscussion(
                messages = messageList.toList(),
                timestamp = messageList.first().timestamp
            )
            previousDiscussions.add(discussion)
            cleanUpOldDiscussions()
            messageList.clear()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cleanUpOldDiscussions() {
        val cutoffDate = LocalDateTime.now().minusDays(30)
        previousDiscussions.removeAll { it.timestamp.isBefore(cutoffDate) }
    }

    fun loadPreviousDiscussion(discussion: PreviousDiscussion) {
        messageList.clear()
        messageList.addAll(discussion.messages)
    }
}

data class PreviousDiscussion(
    val messages: List<MessageModel>,
    val timestamp: LocalDateTime
)
