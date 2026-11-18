package com.example.tablebook.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.material3.SelectableDates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDatePicker(
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val todayMillis = remember { getTodayDateMillis() }
    val selectableDates = remember(todayMillis) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }
        }
    }
    val datePickerState = key(showDatePicker, selectedDateMillis) {
        rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis,
            selectableDates = selectableDates
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Reservation date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = formatReservationDate(selectedDateMillis),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Choose date")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onDateSelected)
                        showDatePicker = false
                    }
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

// Return today's date at midnight in the format used by the Material 3 Date Picker.
fun getTodayDateMillis(): Long {
    val localCalendar = Calendar.getInstance()
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.clear()
    utcCalendar.set(
        localCalendar.get(Calendar.YEAR),
        localCalendar.get(Calendar.MONTH),
        localCalendar.get(Calendar.DAY_OF_MONTH)
    )
    return utcCalendar.timeInMillis
}

fun formatReservationDate(dateMillis: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.UK)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date(dateMillis))
}
