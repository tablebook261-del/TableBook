package uk.ac.tees.mad.tablebook.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.tablebook.model.UserData
import uk.ac.tees.mad.tablebook.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.tablebook.presentation.ViewModel.AuthScreenState
import uk.ac.tees.mad.tablebook.ui.theme.TableBookTheme

@Composable
fun AuthenticationScreen(
    viewModel: AppViewModel,
    onNavigateToHome: () -> Unit
) {
    val authState by viewModel.authScreenState

    AuthenticationContent(
        authState = authState,
        onLogin = { email, password ->
            viewModel.loginUser(UserData(email = email, password = password))
        },
        onRegister = { email, password ->
            viewModel.registerUser(UserData(email = email, password = password))
        },
        onNavigateToHome = onNavigateToHome,
        onResetState = { viewModel.resetAuthState() }
    )
}

@Composable
fun AuthenticationContent(
    authState: AuthScreenState,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String) -> Unit,
    onNavigateToHome: () -> Unit,
    onResetState: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(authState.success) {
        if (authState.success) {
            onNavigateToHome()
            onResetState()
        }
    }

    LaunchedEffect(authState.error) {
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            onResetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLoginMode) "Welcome Back" else "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                if (emailError != null) emailError = null
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = emailError != null,
            supportingText = {
                if (emailError != null) {
                    Text(text = emailError!!)
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                if (passwordError != null) passwordError = null
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = passwordError != null,
            supportingText = {
                if (passwordError != null) {
                    Text(text = passwordError!!)
                }
            },
            trailingIcon = {
                val image = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility")
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                var isValid = true
                if (email.isBlank()) {
                    emailError = "Email cannot be empty"
                    isValid = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Invalid email format"
                    isValid = false
                }
                
                if (password.isBlank()) {
                    passwordError = "Password cannot be empty"
                    isValid = false
                } else if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters"
                    isValid = false
                }

                if (isValid) {
                    if (isLoginMode) {
                        onLogin(email, password)
                    } else {
                        onRegister(email, password)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !authState.isLoading
        ) {
            if (authState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = if (isLoginMode) "Login" else "Register")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                isLoginMode = !isLoginMode
                emailError = null
                passwordError = null
            }
        ) {
            Text(
                text = if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthenticationScreenPreview() {
    TableBookTheme {
        Surface {
            AuthenticationContent(
                authState = AuthScreenState(),
                onLogin = { _, _ -> },
                onRegister = { _, _ -> },
                onNavigateToHome = {},
                onResetState = {}
            )
        }
    }
}
