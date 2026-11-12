package com.example.tablebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tablebook.navigation.AppNavigation
import com.example.tablebook.ui.theme.TableBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TableBookTheme {
                AppNavigation()
            }
        }
    }
}
