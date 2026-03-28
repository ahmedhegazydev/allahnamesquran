package com.example.allahnamesquran

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.allahnamesquran.core.ui.theme.AllahNamesQuranTheme
import com.example.allahnamesquran.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()


        setContent {
            AllahNamesQuranTheme {
                AppNavHost()
            }
        }
    }
}

