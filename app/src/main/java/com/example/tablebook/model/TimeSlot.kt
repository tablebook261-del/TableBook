package com.example.tablebook.model

data class TimeSlot(
    val time: String,
    val status: SlotStatus = SlotStatus.AVAILABLE
)

// Fixed reservation times for the restaurant.
val reservationTimeSlots = listOf(
    TimeSlot("12:00 PM"),
    TimeSlot("1:00 PM"),
    TimeSlot("2:00 PM"),
    TimeSlot("7:00 PM"),
    TimeSlot("8:00 PM")
)
