package com.example.coinscomp.presentation.summary

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinscomp.R
import com.example.coinscomp.core.other.FAILURE_VALUE
import com.example.coinscomp.presentation.summary.models.SummaryUi
import com.example.coinscomp.presentation.uiviews.BottomNavigationBar
import com.example.coinscomp.presentation.uiviews.CustomSnackbar
import com.example.coinscomp.presentation.uiviews.ObserveAsEvents
import com.example.coinscomp.presentation.utils.NavigationItems
import com.example.coinscomp.ui.theme.CoinsCompTheme

private const val SUMMARY_NAV_ITEM_INDEX = 1

@Composable
fun SummaryScreen(
    onNavigationItemSelected: (NavigationItems) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SummaryScreenViewModel = hiltViewModel()

    val summaryUiState by viewModel.summaryUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val okText = stringResource(android.R.string.ok)

    ObserveAsEvents(viewModel.event) { event ->
        if (event.isNotBlank()) {
            snackbarHostState.showSnackbar(
                message = event,
                actionLabel = okText,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

    SummaryScreenContent(
        summaryUiState = summaryUiState,
        snackbarHostState = snackbarHostState,
        onNavigationItemSelected = onNavigationItemSelected,
        modifier = modifier
    )
}

@Composable
fun SummaryScreenContent(
    summaryUiState: SummaryScreenState,
    snackbarHostState: SnackbarHostState,
    onNavigationItemSelected: (NavigationItems) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CustomSnackbar(data)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()

        ) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.summary_statistics),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (summaryUiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    if (summaryUiState.summaryState is SummaryState.Loaded) {
                        val state = summaryUiState.summaryState.value
                        Text(
                            text = state.amountOfDaysAppUsing.trimText(context, R.string.member_for_days),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.totalItemsCount.trimText(context, R.string.total_items_count),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.hiddenCoinsCount.trimText(context, R.string.hidden_coins_count),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.totalNotesCount.trimText(context, R.string.total_notes_count),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = state.dayWithMostNotes.trimText(context, R.string.the_day_on_which_the_most_notes_were_taken),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            // located here (not in Scaffold slots) so that the snackbar would overlap it
            BottomNavigationBar(
                selectedIndex = SUMMARY_NAV_ITEM_INDEX,
                onItemSelected = { item ->
                    onNavigationItemSelected(item)
                }
            )
        }
    }
}

private fun String.trimText(context: Context, @StringRes resource: Int): String {
    return if (this == FAILURE_VALUE) {
        context.getString(resource, this).substringBefore(this) + this
    } else {
        context.getString(resource, this)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SummaryScreenDefaultPreview() {
    CoinsCompTheme {
        SummaryScreenContent(
            summaryUiState = SummaryScreenState(
                summaryState = SummaryState.Default(),
                isLoading = false
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigationItemSelected = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SummaryScreenLoadingPreview() {
    CoinsCompTheme {
        SummaryScreenContent(
            summaryUiState = SummaryScreenState(
                summaryState = SummaryState.Default(),
                isLoading = true
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigationItemSelected = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SummaryScreenLoadedPreview() {
    CoinsCompTheme {
        SummaryScreenContent(
            summaryUiState = SummaryScreenState(
                summaryState = SummaryState.Loaded(
                    SummaryUi(
                        totalItemsCount = "15",
                        hiddenCoinsCount = "3",
                        totalNotesCount = "4",
                        dayWithMostNotes = "21.09.021",
                        amountOfDaysAppUsing = "34"
                    )
                ),
                isLoading = false
            ),
            snackbarHostState = SnackbarHostState(),
            onNavigationItemSelected = {}
        )
    }
}