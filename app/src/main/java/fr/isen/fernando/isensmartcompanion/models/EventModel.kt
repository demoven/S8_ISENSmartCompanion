package fr.isen.fernando.isensmartcompanion.models

import java.io.Serializable

data class EventModel(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String,
    var isNotified: Boolean = false
) : Serializable {

    companion object {
        private var events: List<EventModel> = listOf()

        fun updateEvents(newEvents: List<EventModel>) {
            events = newEvents
        }

        fun getEvents(): List<EventModel> {
            return events
        }
    }
}