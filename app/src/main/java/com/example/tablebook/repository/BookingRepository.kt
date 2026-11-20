package com.example.tablebook.repository

import com.example.tablebook.model.Reservation
import com.example.tablebook.model.ReservationStatus
import com.google.firebase.firestore.FirebaseFirestore

// Reads reservation details for the selected date.
class BookingRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getReservationsForDate(
        date: String,
        onResult: (List<Reservation>?, String?) -> Unit
    ) {
        firestore.collection("reservations")
            .whereEqualTo("date", date)
            .get()
            .addOnSuccessListener { documents ->
                val reservations = documents.documents.mapNotNull { document ->
                    val time = document.getString("time")
                    val status = document.getString("status")

                    if (time == null || status == null) {
                        null
                    } else {
                        Reservation(
                            reservationId = document.id,
                            userId = document.getString("userId") ?: "",
                            date = document.getString("date") ?: date,
                            time = time,
                            status = status,
                            createdAtMillis = document.getLong("createdAtMillis") ?: 0L
                        )
                    }
                }.filter { reservation ->
                    reservation.status == ReservationStatus.CONFIRMED.name
                }

                onResult(reservations, null)
            }
            .addOnFailureListener { error ->
                onResult(
                    null,
                    error.localizedMessage ?: "Could not load reservations. Please try again."
                )
            }
    }
}
