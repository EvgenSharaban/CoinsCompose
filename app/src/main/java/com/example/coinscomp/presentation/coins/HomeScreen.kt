package com.example.coinscomp.presentation.coins

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinscomp.R
import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.coins.models.notes.ModelNotesCustomView
import com.example.coinscomp.presentation.uiviews.BottomNavigationBar
import com.example.coinscomp.presentation.uiviews.CustomSnackbar
import com.example.coinscomp.presentation.uiviews.dialogs.AddNoteAlertDialog
import com.example.coinscomp.presentation.uiviews.dialogs.DeleteNoteAlertDialog
import com.example.coinscomp.presentation.uiviews.dialogs.HideCoinAlertDialog
import com.example.coinscomp.presentation.uiviews.widgets.CustomCoin
import com.example.coinscomp.presentation.uiviews.widgets.CustomNote
import com.example.coinscomp.presentation.utils.NavigationItems
import com.example.coinscomp.ui.theme.CoinsCompTheme

private const val HOME_NAV_ITEM_INDEX = 0

@Composable
fun HomeScreen(
    onNavigationItemSelected: (NavigationItems) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    val loading by viewModel.isLoading.collectAsStateWithLifecycle()
    val itemsList by viewModel.itemsList.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(EventsCoins.None())

    HomeScreenContent(
        itemsList = itemsList,
        loading = loading,
        event = event,
        onCoinClicked = { coin -> viewModel.handleIntent(HomeIntent.CoinClick(coin)) },
        onCoinLongClicked = { coin -> viewModel.handleIntent(HomeIntent.CoinLongClick(coin)) },
        onNoteLongClicked = { note -> viewModel.handleIntent(HomeIntent.NoteLongClick(note)) },
        onAddNote = { noteText -> viewModel.handleIntent(HomeIntent.AddNote(noteText)) },
        onNavigationItemSelected = onNavigationItemSelected,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenContent(
    itemsList: List<CustomViewListItems>,
    loading: Boolean,
    event: EventsCoins,
    onCoinClicked: (ModelCoinsCustomView) -> Unit,
    onCoinLongClicked: (ModelCoinsCustomView) -> Unit,
    onNoteLongClicked: (ModelNotesCustomView) -> Unit,
    onAddNote: (String) -> Unit,
    onNavigationItemSelected: (NavigationItems) -> Unit,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val okText = stringResource(android.R.string.ok)

    val listState = rememberLazyListState()
    val lastVisibleIndex by remember {
        // derivedStateOf tracks changes and recalculates lastVisibleIndex only when necessary, which optimizes performance.
        derivedStateOf { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.lastIndex }
    }

    LaunchedEffect(event) {
        when (event) {
            is EventsCoins.MessageForUser -> {
                val message = event.message
                if (message.isNotBlank()) {
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = okText,
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }

            is EventsCoins.PositionToScrolling -> {
                val item = event
                if (item.position in itemsList.indices && item.position !in listState.firstVisibleItemIndex..lastVisibleIndex) {
                    listState.scrollToItem(item.position)
                }
            }

            is EventsCoins.None -> {}
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
                    ItemList(
                        itemsList = itemsList,
                        listState = listState,
                        onCoinClick = { model -> onCoinClicked(model) },
                        onCoinLongClick = { openHideCoinDialog.value = it },
                        onNoteLongClick = { openDeleteNoteDialog.value = it }
                    )
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
                        .padding(bottom = 16.dp, end = 24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }

                AddNoteAlertDialog(
                    openDialog = openAddNoteDialog,
                    onDismiss = { openAddNoteDialog = false },
                    onConfirmation = { enteredText ->
                        openAddNoteDialog = false
                        onAddNote(enteredText)
                    }
                )

                DeleteNoteAlertDialog(
                    openDialog = openDeleteNoteDialog.value != null,
                    onDismiss = {
                        openDeleteNoteDialog.value = null
                    },
                    onConfirmation = {
                        onNoteLongClicked(openDeleteNoteDialog.value!!)
                        openDeleteNoteDialog.value = null
                    }
                )

                HideCoinAlertDialog(
                    openDialog = openHideCoinDialog.value != null,
                    onDismiss = {
                        openHideCoinDialog.value = null
                    },
                    onConfirmation = {
                        onCoinLongClicked(openHideCoinDialog.value!!)
                        openHideCoinDialog.value = null
                    }
                )
            }

            BottomNavigationBar(
                HOME_NAV_ITEM_INDEX,
                onItemSelected = { item ->
                    onNavigationItemSelected(item)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemList(
    itemsList: List<CustomViewListItems>,
    listState: LazyListState,
    onCoinClick: (ModelCoinsCustomView) -> Unit,
    onCoinLongClick: (ModelCoinsCustomView) -> Unit,
    onNoteLongClick: (ModelNotesCustomView) -> Unit
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = itemsList,
            key = { it.hashCode() } // need for animations
        ) { item ->
            when (item) {
                is CustomViewListItems.CoinItem -> CustomCoin(
                    item = item.coin,
                    onCoinClicked = { onCoinClick(item.coin) },
                    onCoinLongClicked = { onCoinLongClick(item.coin) },
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

                is CustomViewListItems.NoteItem -> CustomNote(
                    note = item.note.note,
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { },
                            onLongClick = { onNoteLongClick(item.note) }
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

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    CoinsCompTheme {
        val notesList = List(5) { index ->
            CustomViewListItems.NoteItem(
                ModelNotesCustomView(
                    id = (index + 1).toString(),
                    note = "note ${index + 1}"
                )
            )
        }
        val coinsList = List(10) { index ->
            CustomViewListItems.CoinItem(
                ModelCoinsCustomView(
                    id = (index + 1).toString(),
                    name = "Bitcoin Fake",
                    rank = index + 1,
                    isActive = true,
                    type = "coin",
                    logo = "https://www.shutterstock.com/image-vector/crypto-currency-golden-coin-black-600nw-593193626.jpg",
                    description = "The first and most popular cryptocurrency. The first and most popular cryptocurrency. The first and most popular cryptocurrency. The first and most popular cryptocurrency.",
                    isHided = false,
                    isExpanded = false,
                    price = 2432.23,
                    shortName = "BTC",
                    creationDate = "21.06.1990"
                )
            )
        }
        val sampleItems = notesList.plus(coinsList)
        HomeScreenContent(
            itemsList = sampleItems,
            loading = false,
            event = EventsCoins.None(),
            onCoinClicked = {},
            onCoinLongClicked = {},
            onNoteLongClicked = {},
            onAddNote = {},
            onNavigationItemSelected = {},
            modifier = Modifier
        )
    }
}