package com.example.coinscomp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coinscomp.presentation.coins.HomeScreen
import com.example.coinscomp.presentation.summary.SummaryScreen
import com.example.coinscomp.presentation.utils.NavigationItems
import com.example.coinscomp.presentation.utils.NavigationTypes.HomeType
import com.example.coinscomp.presentation.utils.NavigationTypes.SummaryType
import com.example.coinscomp.ui.theme.CoinsCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InitNavigationBetweenScreens()
        }
    }

    @Composable
    private fun InitNavigationBetweenScreens() {
        val navController = rememberNavController()
        val doOnNavigationItemSelected: (NavigationItems) -> Unit = { item ->
            val destination = when (item) {
                NavigationItems.HOME_NAV_ITEM -> HomeType
                NavigationItems.SUMMARY_NAV_ITEM -> SummaryType
            }
            navController.navigate(route = destination)
        }
        CoinsCompTheme {
            NavHost(
                navController,
                startDestination = HomeType
            ) {
                composable<HomeType> {
                    HomeScreen(doOnNavigationItemSelected)
                    BackHandler {
                        finish()
                    }
                }
                composable<SummaryType> {
                    SummaryScreen(doOnNavigationItemSelected)
                    BackHandler {
                        navController.navigate(HomeType) {
                            popUpTo(HomeType) { inclusive = false }
                        }
                    }
                }
            }
        }
    }
}