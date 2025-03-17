package com.example.coinscomp.presentation.uiviews

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinscomp.R
import com.example.coinscomp.core.other.TAG
import com.example.coinscomp.presentation.coins.CustomViewListItems
import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.coins.models.notes.ModelNotesCustomView
import com.example.coinscomp.presentation.summary.SummaryActivity
import com.example.coinscomp.presentation.uiviews.dialogs.AddNoteAlertDialog
import com.example.coinscomp.presentation.uiviews.dialogs.DeleteNoteAlertDialog
import com.example.coinscomp.presentation.uiviews.dialogs.HideCoinAlertDialog
import com.example.coinscomp.presentation.uiviews.views.CoinCustomView
import com.example.coinscomp.presentation.uiviews.views.NoteCustomView
import com.example.coinscomp.presentation.utils.BottomNavigationItem
import com.example.coinscomp.ui.theme.CoinsCompTheme

private const val HOME_NAV_ITEM_INDEX = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    itemsList: List<CustomViewListItems>,
    positionToScrolling: Int,
    errorMessage: String?,
    loading: Boolean = false,
    onNoteAdded: (String) -> Unit,
    onNoteLongClicked: (ModelNotesCustomView) -> Unit,
    onCoinLongClicked: (ModelCoinsCustomView) -> Unit,
    onCoinClicked: (ModelCoinsCustomView) -> Unit
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val okText = stringResource(android.R.string.ok)

    val listState = rememberLazyListState()
    val lastVisibleIndex by remember {
        // derivedStateOf tracks changes and recalculates lastVisibleIndex only when necessary, which optimizes performance.
        derivedStateOf { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.lastIndex }
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

    LaunchedEffect(positionToScrolling) {
        Log.d(TAG, "HomeScreen: lase visible index = $lastVisibleIndex")
        if (positionToScrolling in itemsList.indices && positionToScrolling !in listState.firstVisibleItemIndex..lastVisibleIndex) {
            listState.scrollToItem(positionToScrolling)
        }
    }

    BackHandler {
        (context as? Activity)?.finishAffinity()
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
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                var openAddNoteDialog by remember { mutableStateOf(false) }
                val openDeleteNoteDialog = remember { mutableStateOf<ModelNotesCustomView?>(null) }
                val openHideCoinDialog = remember { mutableStateOf<ModelCoinsCustomView?>(null) }

                if (itemsList.isEmpty()) {
                    if (!loading) {
                        Text(
                            text = stringResource(R.string.no_data),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(state = listState) {
                        items(
                            items = itemsList,
                            key = { it.hashCode() } // need for animations
                        ) { item ->
                            when (item) {
                                is CustomViewListItems.CoinItem -> CoinCustomView(
                                    rank = item.coin.rank.toString(),
                                    name = item.coin.name,
                                    price = item.coin.price.toString(),
                                    description = item.coin.description,
                                    creationDate = item.coin.creationDate,
                                    shortName = item.coin.shortName,
                                    logo = item.coin.logo,
                                    isExpanded = item.coin.isExpanded,
                                    onCoinClicked = {
                                        onCoinClicked(item.coin)
                                    },
                                    onCoinLongClicked = {
                                        openHideCoinDialog.value = item.coin
                                    },
                                    modifier = Modifier
                                        .animateItem(
                                            fadeInSpec = null,
                                            fadeOutSpec = null,
                                            placementSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                )

                                is CustomViewListItems.NoteItem -> NoteCustomView(
                                    note = item.note.note,
                                    modifier = Modifier
                                        .combinedClickable(
                                            onClick = { },
                                            onLongClick = {
                                                openDeleteNoteDialog.value = item.note
                                            }
                                        )
                                        .animateItem(
                                            placementSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                )
                            }
                        }
                    }
                }

                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                FloatingActionButton(
                    onClick = {
                        openAddNoteDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 32.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }

                if (openAddNoteDialog) {
                    AddNoteAlertDialog(
                        onDismiss = { openAddNoteDialog = false },
                        onConfirmation = { enteredText ->
                            openAddNoteDialog = false
                            onNoteAdded(enteredText)
                        }
                    )
                }

                if (openDeleteNoteDialog.value != null) {
                    DeleteNoteAlertDialog(
                        onDismiss = {
                            openDeleteNoteDialog.value = null
                        },
                        onConfirmation = {
                            onNoteLongClicked(openDeleteNoteDialog.value!!)
                            openDeleteNoteDialog.value = null
                        }
                    )
                }

                if (openHideCoinDialog.value != null) {
                    HideCoinAlertDialog(
                        onDismiss = {
                            openHideCoinDialog.value = null
                        },
                        onConfirmation = {
                            onCoinLongClicked(openHideCoinDialog.value!!)
                            openHideCoinDialog.value = null
                        }
                    )
                }
            }
            BottomNavigationBar(
                HOME_NAV_ITEM_INDEX,
                onItemSelected = { item ->
                    moveToSummaryActivity(context, item)
                }
            )
        }
    }
}

private fun moveToSummaryActivity(context: Context, item: BottomNavigationItem) {
    if (item.idRes == R.id.nav_summary) {
        val intent = Intent(context, SummaryActivity::class.java)
        val resetDefaultAnimation = ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle()
        context.startActivity(intent, resetDefaultAnimation)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    CoinsCompTheme {
        HomeScreen(
            itemsList = emptyList(),
            loading = false,
            positionToScrolling = 0,
            errorMessage = null,
            onNoteAdded = {},
            onNoteLongClicked = {},
            onCoinLongClicked = {},
            onCoinClicked = {}
        )
    }
}