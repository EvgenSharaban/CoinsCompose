package com.example.coinscomp.presentation.summary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coinscomp.ui.theme.CoinsCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CoinsCompTheme {
                SummaryScreen()
            }
        }
    }
}