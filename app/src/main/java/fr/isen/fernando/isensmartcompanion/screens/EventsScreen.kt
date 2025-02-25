package fr.isen.fernando.isensmartcompanion.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class EventsScreen {

    @Composable
    fun EventScreen() {

            Button(
                onClick = {
                    Log.d("eventscreen", "onclick")
                },
                content = {
                    Text("Temporary button")
                }
            )
    }

}