package fr.isen.fernando.isensmartcompanion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.isen.fernando.isensmartcompanion.database.AppDatabase
import fr.isen.fernando.isensmartcompanion.database.PairDAO
import fr.isen.fernando.isensmartcompanion.database.PairQuestionAnswer
import fr.isen.fernando.isensmartcompanion.models.EventModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(db: AppDatabase) {
    // Liste des items de l'historique, utilisée pour gérer l'état
    val historyItems = remember { mutableStateListOf<PairQuestionAnswer>() }

    // Charge les données une seule fois au démarrage
    LaunchedEffect(Unit) {
        val datas = db.pairDAO().getAll()
        historyItems.clear()
        historyItems.addAll(datas)
    }

    Column(modifier = Modifier.fillMaxWidth()
        .padding(top = 40.dp)) {
        // Bouton pour supprimer tout le contenu
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                db.pairDAO().deleteAll()
            }
            CoroutineScope(Dispatchers.Main).launch {
                // Vider aussi la liste affichée
                historyItems.clear()
            }
        },
            modifier = Modifier.padding(top = 10.dp)) {
            Text("Tout supprimer")
        }

        // Affichage de la liste avec LazyColumn
        LazyColumn {
            items(historyItems) { pair ->
                pairRow(pair, db, historyItems)
            }
        }
    }
}

@Composable
fun pairRow(pair: PairQuestionAnswer, db: AppDatabase, historyItems: MutableList<PairQuestionAnswer>) {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = pair.question)
            Text(text = pair.answer)
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    db.pairDAO().delete(pair)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    historyItems.remove(pair)  // Supprimer de la liste affichée
                }
            },
                modifier = Modifier.background(Color.Red)) {
                Text("Supprimer")
            }
        }
    }
}

