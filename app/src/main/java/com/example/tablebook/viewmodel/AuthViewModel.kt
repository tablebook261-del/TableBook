package com.example.tablebook.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tablebook.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Holds the authentication screen state and communicates with the repository.
class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(repository.currentUserExists())
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun login(email: String, password: String) {
        if (!validateInput(email, password)) return

        _isLoading.value = true
        _errorMessage.value = null

        repository.login(email.trim(), password) { success, error ->
            _isLoading.value = false
            if (success) {
                _isAuthenticated.value = true
            } else {
                _errorMessage.value = error
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (!validateInput(email, password)) return

        if (password != confirmPassword) {
            _errorMessage.value = "Passwords do not match."
            return
        }

        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters long."
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        repository.register(email.trim(), password) { success, error ->
            _isLoading.value = false
            if (success) {
                _isAuthenticated.value = true
            } else {
                _errorMessage.value = error
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun logout() {
        repository.logout()
        _isAuthenticated.value = false
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Please enter your email and password."
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _errorMessage.value = "Please enter a valid email address."
            return false
        }

        _errorMessage.value = null
        return true
    }
}
