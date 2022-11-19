package com.example.myapplication

data class EventModel(
    val eventType: String,
    val dataOfEvent: String,
    val eventDescription: String,
    val Color: Int
) : java.io.Serializable
