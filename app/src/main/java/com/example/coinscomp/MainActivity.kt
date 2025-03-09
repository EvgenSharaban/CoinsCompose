package com.example.coinscomp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coinscomp.ui.theme.CoinsCompTheme
import com.example.coinscomp.ui.training.TrainingAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinsCompTheme {
                TrainingAppScreen()
            }
        }
    }
}