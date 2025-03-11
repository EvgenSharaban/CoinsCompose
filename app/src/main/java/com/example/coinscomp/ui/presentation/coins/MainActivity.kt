package com.example.coinscomp.ui.presentation.coins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coinscomp.ui.theme.CoinsCompTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinsCompTheme {
//                TrainingAppScreen()
                HomeScreen()
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