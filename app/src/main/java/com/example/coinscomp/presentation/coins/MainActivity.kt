package com.example.coinscomp.presentation.coins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val scrollingListPosition = remember { mutableIntStateOf(-1) }

            when (event) {
                is EventsCoins.MessageForUser -> {
                    val message = (event as EventsCoins.MessageForUser).message

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

//                TrainingAppScreen()

//                CustomCard(
//                    rank = "5",
//                    name = "Bitcoin Bitcoin",
//                    price = "Price: 933532.325345 USD",
//                    description = "skjd skjahg hjf hjkkgfg hsdh skjd skjahg hjf hjkkgfg hsdh kkgfgdjghksjdhgksdhkhkgshkgshkh hsghkhk hkjhsgk hkshksghkhgkjhkjsgh sghk khkdhgjkshk  hsdh skjhgh sghskghskjd skjahg hjf hjkkgfg hsdh skjhgh sghskgh",
//                    creationDate = "Since 2009.12.12",
//                    shortName = "BTC",
//                    logoPainter = painterResource(R.drawable.case_detail_sample),
//                )
            }
        }
    }
}