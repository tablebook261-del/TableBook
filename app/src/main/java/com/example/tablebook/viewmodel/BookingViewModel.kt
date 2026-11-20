package com.example.tablebook.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tablebook.model.Reservation
import com.example.tablebook.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Holds the reservation information shown on the Home screen.
data class BookingScreenState(
    val isLoading: Boolean = false,
    val reservations: List<Reservation> = emptyList(),
    val errorMessage: String? = null
) {
    val bookedTimes: Set<String>
        get() = reservations.map { it.time }.toSet()
}

class BookingViewModel : ViewModel() {
    private val repository = BookingRepository()

    private val _screenState = MutableStateFlow(BookingScreenState())
    val screenState: StateFlow<BookingScreenState> = _screenState.asStateFlow()

    fun loadReservations(date: String) {
        _screenState.value = BookingScreenState(isLoading = true)

        repository.getReservationsForDate(date) { reservations, error ->
            if (error != null) {
                _screenState.value = BookingScreenState(errorMessage = error)
            } else {
                _screenState.value = BookingScreenState(
                    reservations = reservations ?: emptyList()
                )
            }
        }
    }
}
