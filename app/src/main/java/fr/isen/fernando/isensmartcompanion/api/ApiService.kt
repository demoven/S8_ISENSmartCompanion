package fr.isen.fernando.isensmartcompanion.api

import fr.isen.fernando.isensmartcompanion.models.EventModel
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("events.json")
    fun getEvents(): Call<List<EventModel>>
}