package com.example.geminichat

import ChatViewModel
import PreviousDiscussion
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Typography
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.geminichat.ui.theme.*
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        var isDarkTheme by mutableStateOf(false)

        setContent {
            GeminiChatTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(innerPadding)
                    ) {
                        SlidingMenuContainer(
                            viewModel = chatViewModel,
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { isDarkTheme = !isDarkTheme }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SlidingMenuContainer(
    viewModel: ChatViewModel,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    onThemeChange: () -> Unit
) {
    var offsetX by remember { mutableStateOf(-250f) }
    var switchChecked by remember { mutableStateOf(isDarkTheme) }

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
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onBackground),///////////////////////
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colorScheme.onBackground


            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.recent),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    var menus by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
                    ) {
                        IconButton(onClick = { menus = true }) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        DropdownMenu(
                            expanded = menus,
                            onDismissRequest = { menus = false },
                            Modifier.background(MaterialTheme.colorScheme.background),
                            offset = DpOffset(x = 0.dp, y = 0.dp) // Align directly below the icon
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    menus = false
                                    onThemeChange()
                                }
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        if (isDarkTheme) "Light" else "Dark",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Switch(
                                        checked = switchChecked,
                                        onCheckedChange = {
                                            switchChecked = it
                                            onThemeChange()
                                        }
                                    )
                                }
                            }
                            DropdownMenuItem(
                                onClick = {
                                    menus = false
                                    viewModel.previousDiscussions.clear()
                                }
                            ) {
                                Text("Supprimer tout", color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            DisplayDiscussions(viewModel.previousDiscussions, viewModel)
            Spacer(modifier = Modifier.weight(1f))
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
                DiscussionSection(
                    stringResource(id = R.string.hier),
                    yesterdayDiscussions,
                    viewModel
                )
            }
        }
        if (last7DaysDiscussions.isNotEmpty()) {
            item {
                DiscussionSection(
                    stringResource(id = R.string.seven),
                    last7DaysDiscussions,
                    viewModel
                )
            }
        }
        if (last30DaysDiscussions.isNotEmpty()) {
            item {
                DiscussionSection(
                    stringResource(id = R.string.THRT),
                    last30DaysDiscussions,
                    viewModel
                )
            }
        }
    }
}

@Composable
fun DiscussionSection(
    title: String,
    discussions: List<PreviousDiscussion>,
    viewModel: ChatViewModel
 ) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        discussions.forEach { discussion ->
            val firstMessage =
                discussion.messages.firstOrNull()?.message ?: stringResource(id = R.string.ampty)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .clickable {
                        viewModel.loadPreviousDiscussion(discussion)
                    },
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colorScheme.background
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
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            text = discussion.timestamp.toLocalDate().toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    IconButton(onClick = { viewModel.removeDiscussion(discussion) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = black,
    onBackground = black,
    onSecondary = whiteDove,
    secondary = gray,
    tertiary = blackapp,
    onTertiary = white,
    onPrimary = gray,
    background = darckgrey,
    onSurface = whiteDove,
    surfaceVariant = Pink40,
    surfaceTint = gray
)

private val LightColorScheme = lightColorScheme(
    primary = bleuApp,
    onBackground = whiteDove,
    onSecondary = bleuApp,
    secondary = white,
    onTertiary = gray,
    tertiary = whiteDove,
    onPrimary = darkbleu,
    background = whiteDove,
    onSurface = black,
    surfaceVariant = bleu,
    surfaceTint = Vertpale


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

@Composable
fun GeminiChatTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}