package com.example.coinscomp.presentation.summary

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinscomp.R
import com.example.coinscomp.core.other.FAILURE_VALUE
import com.example.coinscomp.presentation.coins.MainActivity
import com.example.coinscomp.presentation.uiviews.BottomNavigationBar
import com.example.coinscomp.presentation.uiviews.CustomSnackbar
import com.example.coinscomp.ui.theme.CoinsCompTheme

private const val SUMMARY_NAV_ITEM_INDEX = 1

@Composable
fun SummaryScreen(
    summaryScreenState: SummaryScreenState,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val okText = stringResource(android.R.string.ok)

    BackHandler {
        moveToHomePage(context)
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = okText,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

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
                if (summaryScreenState.isLoading) {
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
                    if (summaryScreenState.summaryState is SummaryState.Loaded) {
                        val state = summaryScreenState.summaryState.value
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
            BottomNavigationBar(
                selectedIndex = SUMMARY_NAV_ITEM_INDEX,
                onItemSelected = { item ->
                    if (item.idRes == R.id.nav_home) {
                        moveToHomePage(context)
                    }
                }
            )
        }
    }
}

private fun moveToHomePage(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    val resetDefaultAnimation = ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle()
    context.startActivity(intent, resetDefaultAnimation)
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
private fun SummaryScreenPreview() {
    CoinsCompTheme {
        SummaryScreen(
            summaryScreenState = SummaryScreenState.DEFAULT,
            errorMessage = ""
        )
    }
}