package fr.isen.fernando.isensmartcompanion.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.fernando.isensmartcompanion.api.NetworkManager
import fr.isen.fernando.isensmartcompanion.models.EventModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EventScreen(eventHandler: (EventModel) -> Unit) {
    var events = remember { mutableStateOf<List<EventModel>>(listOf()) }
    LaunchedEffect(Unit) {
        val call = NetworkManager.api.getEvents()
        call.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(
                p0: Call<List<EventModel>>,
                p1: Response<List<EventModel>>
            ) {
                events.value = p1.body() ?: listOf()
            }

            override fun onFailure(p0: Call<List<EventModel>>, p1: Throwable) {
                Log.e("request", p1.message ?: "request")
            }
        })
    }

    Column(modifier = Modifier.padding(vertical = 52.dp)) {
        LazyColumn {
            items(events.value) { event ->
                EventRow(event, eventHandler)
            }
        }
    }
}

@Composable
fun EventRow(event: EventModel, eventHandler: (EventModel) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { eventHandler(event) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(event.date)
            Text(event.description, modifier = Modifier.padding(top = 8.dp))
        }
    }
}


