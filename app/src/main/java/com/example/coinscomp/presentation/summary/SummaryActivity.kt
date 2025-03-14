package com.example.coinscomp.presentation.summary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinscomp.presentation.uiviews.SummaryScreen
import com.example.coinscomp.ui.theme.CoinsCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryActivity : ComponentActivity() {

    private val viewModel: SummaryActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val errorMessage by viewModel.event.collectAsStateWithLifecycle(null)
            val summaryUiState by viewModel.summaryUiState.collectAsStateWithLifecycle()

            CoinsCompTheme {
                SummaryScreen(
                    summaryScreenState = summaryUiState,
                    errorMessage = errorMessage
                )
            }
        }
    }
}