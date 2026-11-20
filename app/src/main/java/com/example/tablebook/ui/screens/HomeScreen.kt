package com.example.tablebook.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tablebook.ui.components.ReservationCard
import com.example.tablebook.ui.components.ReservationDatePicker
import com.example.tablebook.ui.components.TimeSlotItem
import com.example.tablebook.ui.components.getTodayDateMillis
import com.example.tablebook.model.SlotStatus
import com.example.tablebook.model.reservationTimeSlots
import com.example.tablebook.viewmodel.BookingViewModel
import androidx.compose.runtime.collectAsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun HomeScreen() {
    var selectedDateMillis by remember {
        mutableLongStateOf(getTodayDateMillis())
    }
    val bookingViewModel: BookingViewModel = viewModel()
    val bookingState by bookingViewModel.screenState.collectAsState()

    // Load bookings whenever the selected date changes.
    LaunchedEffect(selectedDateMillis) {
        bookingViewModel.loadReservations(formatFirestoreDate(selectedDateMillis))
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "TableBook",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Book your table with ease.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            ReservationDatePicker(
                selectedDateMillis = selectedDateMillis,
                onDateSelected = { selectedDateMillis = it }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Choose your time",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Available reservation times",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (bookingState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            bookingState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            reservationTimeSlots.forEach { slot ->
                val status = when {
                    bookingState.isLoading -> "Loading..."
                    bookingState.errorMessage != null -> "Unavailable"
                    bookingState.bookedTimes.contains(slot.time) -> SlotStatus.BOOKED.displayName
                    else -> SlotStatus.AVAILABLE.displayName
                }

                TimeSlotItem(
                    time = slot.time,
                    status = status
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "My reservation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            ReservationCard()
        }
    }
}


// Use the same date format for Firestore queries and saved reservations.
private fun formatFirestoreDate(dateMillis: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date(dateMillis))
}
