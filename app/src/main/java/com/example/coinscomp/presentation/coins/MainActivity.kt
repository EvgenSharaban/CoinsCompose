package com.example.coinscomp.presentation.coins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinscomp.presentation.coins.uiviews.HomeScreen
import com.example.coinscomp.ui.theme.CoinsCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val coinsList by viewModel.coinsList.collectAsStateWithLifecycle()

            CoinsCompTheme {
//                TrainingAppScreen()
                HomeScreen(coins = coinsList)
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