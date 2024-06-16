package com.example.geminichat

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class ConversationModel @RequiresApi(Build.VERSION_CODES.O) constructor(
    val title: String,
    val date: LocalDate = LocalDate.now(),
    var messages: List<MessageModel> = emptyList(),
    val isCurrent: Boolean
)
