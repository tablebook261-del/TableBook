package uk.ac.tees.mad.tablebook.presentation.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import uk.ac.tees.mad.tablebook.domain.Repo
import uk.ac.tees.mad.tablebook.model.ResultState
import uk.ac.tees.mad.tablebook.model.UserData

class AppViewModel(private val repo: Repo): ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _AuthScreenState = mutableStateOf(AuthScreenState())
    val authScreenState = _AuthScreenState
    fun loginUser(userData: UserData) {
        viewModelScope.launch {
            repo.loginuserwithemailandpassword(userData).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _AuthScreenState.value =
                            AuthScreenState(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _AuthScreenState.value =
                            AuthScreenState(
                                success = true,
                                userdata = result.data
                            )
                    }

                    is ResultState.error -> {
                        _AuthScreenState.value =
                            AuthScreenState(error = result.message)
                    }
                }
            }
        }
    }
    fun resetAuthState() {
        _AuthScreenState.value = AuthScreenState()
    }
    fun registerUser(userData: UserData) {
        viewModelScope.launch {
            repo.registeruserwithemailandpassword(userData).collect { result ->
                when (result) {

                    ResultState.Loading -> {
                        _AuthScreenState.value =
                            AuthScreenState(isLoading = true)
                    }

                    is ResultState.Succes -> {
                        _AuthScreenState.value =
                            AuthScreenState(
                                success = true,
                                userdata = result.data
                            )
                    }

                    is ResultState.error -> {
                        _AuthScreenState.value =
                            AuthScreenState(error = result.message)
                    }
                }
            }
        }
    }
    fun logout() {
        auth.signOut()
    }

}
data class AuthScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userdata: String? = null,
    val success: Boolean = false
)