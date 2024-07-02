
package com.example.geminichat

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class MessageModel (
    val message : String,
    val rool: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)