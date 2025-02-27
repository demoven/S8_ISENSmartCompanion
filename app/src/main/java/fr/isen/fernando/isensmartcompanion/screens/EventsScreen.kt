package fr.isen.fernando.isensmartcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.fernando.isensmartcompanion.models.EventModel

    @Composable
    fun EventScreen(eventHandler: (EventModel) -> Unit) {

        Column (modifier = Modifier.padding(top = 40.dp)){
           val events = EventModel.getEvents()
            LazyColumn {
                items(events){ event ->
                    EventRow(event, eventHandler)
                    }

                }
            }
        }
@Composable
fun EventRow(event: EventModel, eventHandler:(EventModel) -> Unit) {
    Card (
        modifier = Modifier
            .padding(16.dp)
            .clickable { eventHandler(event) }
    ){
        Column (modifier = Modifier.padding(16.dp)) {
            Row (horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(event.title)
                Text(event.date)
            }

            Text(event.description)
        }
    }
}


