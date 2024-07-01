
package com.example.geminichat

import java.time.LocalDateTime

data class MessageModel(
    val message : String,
    val rool: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)