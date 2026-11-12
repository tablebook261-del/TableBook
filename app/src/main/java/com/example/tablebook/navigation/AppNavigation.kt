package com.example.tablebook.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        composable(route = AppRoutes.SPLASH) {
            PlaceholderScreen(
                title = "TableBook",
                message = "Splash screen will be developed next."
            )
        }

        composable(route = AppRoutes.LOGIN) {
            PlaceholderScreen(
                title = "Login",
                message = "Login screen will be developed next."
            )
        }

        composable(route = AppRoutes.REGISTER) {
            PlaceholderScreen(
                title = "Register",
                message = "Register screen will be developed next."
            )
        }

        composable(route = AppRoutes.HOME) {
            PlaceholderScreen(
                title = "Home",
                message = "Home screen will be developed in Sprint 2."
            )
        }

        composable(route = AppRoutes.RESERVATION_DETAILS) {
            PlaceholderScreen(
                title = "Reservation Details",
                message = "Reservation details will be developed in Sprint 3."
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    message: String
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
