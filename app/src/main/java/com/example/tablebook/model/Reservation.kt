package com.example.tablebook.model

data class Reservation(
    val reservationId: String = "",
    val userId: String = "",
    val date: String = "",
    val time: String = "",
    val status: String = ReservationStatus.CONFIRMED.name,
    val createdAtMillis: Long = 0L
)

enum class ReservationStatus {
    CONFIRMED,
    CANCELLED
}
