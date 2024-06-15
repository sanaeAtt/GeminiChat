package com.example.geminichat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messageList by lazy {
        mutableStateListOf<MessageModel>(

        )
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apiKey
    )

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
            }catch (e : Exception){
                messageList.removeLast()
                messageList.add(MessageModel("Error: "+e.message.toString(),"model"))
            }

        }
    }
}