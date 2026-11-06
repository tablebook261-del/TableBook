package uk.ac.tees.mad.tablebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.tablebook.data.RepoImpl
import uk.ac.tees.mad.tablebook.presentation.ViewModel.AppViewModel
import uk.ac.tees.mad.tablebook.presentation.screens.AuthenticationScreen
import uk.ac.tees.mad.tablebook.ui.theme.TableBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TableBookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val appViewModel: AppViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return AppViewModel(RepoImpl()) as T
                            }
                        }
                    )
                    
                    var currentRoute by remember { mutableStateOf("Auth") }

                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        when (currentRoute) {
                            "Auth" -> {
                                AuthenticationScreen(
                                    viewModel = appViewModel,
                                    onNavigateToHome = {
                                        currentRoute = "Home"
                                    }
                                )
                            }
                            "Home" -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = "Home Screen Placeholder")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}