package com.example.coinscomp.presentation.coins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinscomp.core.other.DEFAULT_SCROLLING_POSITION
import com.example.coinscomp.presentation.coins.models.notes.NoteUiModelMapper.mapToRoomModel
import com.example.coinscomp.presentation.uiviews.HomeScreen
import com.example.coinscomp.ui.theme.CoinsCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val loading by viewModel.isLoading.collectAsStateWithLifecycle()
            val itemsList by viewModel.itemsList.collectAsStateWithLifecycle()
            val event by viewModel.event.collectAsStateWithLifecycle(EventsCoins.None())

            val scrollingListPosition = remember { mutableIntStateOf(DEFAULT_SCROLLING_POSITION) }
            val errorMessage = remember { mutableStateOf<String?>(null) }

            when (event) {
                is EventsCoins.MessageForUser -> {
                    val message = (event as EventsCoins.MessageForUser).message
                    errorMessage.value = message
                }

                is EventsCoins.PositionToScrolling -> {
                    val position = (event as EventsCoins.PositionToScrolling).position
                    scrollingListPosition.intValue = position
                }

                is EventsCoins.None -> {}
            }

            CoinsCompTheme {
                HomeScreen(
                    itemsList = itemsList,
                    loading = loading,
                    positionToScrolling = scrollingListPosition.intValue,
                    errorMessage = errorMessage.value,
                    onNoteAdded = { enteredNote ->
                        viewModel.addNote(enteredNote)
                    },
                    onNoteLongClicked = { note ->
                        viewModel.onItemNoteLongClicked(note.mapToRoomModel())
                    },
                    onCoinLongClicked = { coin ->
                        viewModel.onItemCoinLongClicked(coin)
                    },
                    onCoinClicked = { coin ->
                        viewModel.onItemCoinClicked(coin)
                    }
                )
                // need to scrolling after deleting and adding all notes
                scrollingListPosition.intValue = DEFAULT_SCROLLING_POSITION
            }
        }
    }
}