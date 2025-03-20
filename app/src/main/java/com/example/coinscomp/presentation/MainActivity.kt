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
import com.example.coinscomp.presentation.coins.Home
import com.example.coinscomp.presentation.coins.HomeScreen
import com.example.coinscomp.presentation.summary.Summary
import com.example.coinscomp.presentation.summary.SummaryScreen
import com.example.coinscomp.presentation.utils.NavigationItems
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
                NavigationItems.HOME -> Home
                NavigationItems.SUMMARY -> Summary
            }
            navController.navigate(route = destination)
        }
        CoinsCompTheme {
            NavHost(
                navController,
                startDestination = Home
            ) {
                composable<Home> {
                    HomeScreen(doOnNavigationItemSelected)
                    BackHandler {
                        finish()
                    }
                }
                composable<Summary> {
                    SummaryScreen(doOnNavigationItemSelected)
                    BackHandler {
                        navController.navigate(Home) {
                            popUpTo(Home) { inclusive = false }
                        }
                    }
                }
            }
        }
    }
}