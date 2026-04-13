package com.example.paysrest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.paysrest.ui.theme.MainNavigation
import com.example.paysrest.ui.theme.PaysrestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaysrestTheme {
                MainNavigation()
            }
        }
    }
}