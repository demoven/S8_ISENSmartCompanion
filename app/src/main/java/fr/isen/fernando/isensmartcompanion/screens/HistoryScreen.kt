package fr.isen.fernando.isensmartcompanion.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import fr.isen.fernando.isensmartcompanion.R
import fr.isen.fernando.isensmartcompanion.database.AppDatabase
import fr.isen.fernando.isensmartcompanion.database.PairQuestionAnswer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HistoryScreen(db: AppDatabase) {
    val context = LocalContext.current

    val historyItems = remember { mutableStateListOf<PairQuestionAnswer>() }

    LaunchedEffect(Unit) {
        val datas = db.pairDAO().getAll()
        historyItems.clear()
        historyItems.addAll(datas)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .padding(bottom = 90.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {


            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.pairDAO().deleteAll()
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        historyItems.clear()
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = context.getString(R.string.delete_all),
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.Red)
                )
            }
        }

        LazyColumn {
            items(historyItems) { pair ->
                pairRow(pair, db, historyItems, context)
            }
        }
    }
}

@Composable
fun pairRow(
    pair: PairQuestionAnswer,
    db: AppDatabase,
    historyItems: MutableList<PairQuestionAnswer>,
    context: Context
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(pair.date,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),)
            IconButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    db.pairDAO().delete(pair)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    historyItems.remove(pair)
                }
            })
            {
                Image(
                    painter = painterResource(R.drawable.xmark),
                    contentDescription = context.getString(R.string.delete),
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.Red)
                )
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text("${getString(context, R.string.question)}: ${pair.question}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),)
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "${getString(context, R.string.answer)}: ${pair.answer}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            )

        }
    }
}

