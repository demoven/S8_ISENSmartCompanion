package fr.isen.fernando.isensmartcompanion.screens


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.room.Room
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import fr.isen.fernando.isensmartcompanion.R
import fr.isen.fernando.isensmartcompanion.database.AppDatabase
import fr.isen.fernando.isensmartcompanion.database.PairQuestionAnswer
import fr.isen.fernando.isensmartcompanion.models.ChatModel
import fr.isen.fernando.isensmartcompanion.models.EventModel
import fr.isen.fernando.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@Composable
    fun MessageCard(title: String, db: AppDatabase) {
        val context = LocalContext.current
        var userInput = remember { mutableStateOf("") }

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyDdrM0fHqVaolZSadXvI0qcbCs-kHh-4ck"
    )
    var responseText by remember{ mutableStateOf<String?>(null)}
    val coroutineScope = rememberCoroutineScope()
    var historyAI by remember { mutableStateOf<List<Content>>(listOf())}
    var chats = remember{ mutableStateListOf<ChatModel>()}
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.isen_logo),
                    contentDescription = context.getString(R.string.isen_logo),
                    modifier = Modifier.size(150.dp)
                )
                Text(text = title, textAlign = TextAlign.Center)
                Row (modifier = Modifier.weight(0.5f)
                    .fillMaxSize()){
                    LazyColumn{
                        items(chats){ chat ->
                            chatRow(chat)
                        }
                    }
                }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(8.dp)

            ) {
                TextField(
                    value = userInput.value,
                    onValueChange = { userInput.value = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "${getString(context, R.string.user_input)} : ${userInput.value}",
                                    Toast.LENGTH_LONG
                                ).show()
                                if(userInput.value != "") {
                                    coroutineScope.launch {
                                        val chat = generativeModel.startChat(
                                            history = historyAI
                                        )
                                        val message = chat.sendMessage(userInput.value)
                                        historyAI = historyAI + listOf(
                                            content(role = "user"){text(userInput.value)},
                                            content(role = "model"){text(message.text.toString())}
                                        )
                                        chats.add(ChatModel("user", userInput.value))
                                        userInput.value = ""
                                        chats.add(ChatModel("model", message.text.toString()))
                                        CoroutineScope(Dispatchers.IO).launch {
                                            db.pairDAO().insert(PairQuestionAnswer(question = userInput.value, answer = message.text.toString(), date = format.format(Date(System.currentTimeMillis()))))
                                        }
                                        }
                                }
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Red, shape = CircleShape)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_forward),
                                contentDescription = getString(context, R.string.envoyer)
                            )
                        }
                    }
                )
            }
        }
}

@Composable
fun chatRow(chat: ChatModel) {
    Card(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = chat.message,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}