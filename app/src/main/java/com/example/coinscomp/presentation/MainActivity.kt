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
import com.example.coinscomp.presentation.utils.NavigationItems.HOME_ITEM
import com.example.coinscomp.presentation.utils.NavigationItems.SUMMARY_ITEM
import com.example.coinscomp.ui.theme.CoinsCompTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Navigation()
        }
    }

    @Composable
    private fun Navigation() {
        val navController = rememberNavController()
        val doOnNavigationItemSelected: (NavigationItems) -> Unit = { item ->
            navController.navigate(route = item.route)
        }
        CoinsCompTheme {
            NavHost(
                navController,
                startDestination = HOME_ITEM.route
            ) {
                composable(HOME_ITEM.route) {
                    HomeScreen(doOnNavigationItemSelected)
                    BackHandler {
                        finish()
                    }
                }
                composable(SUMMARY_ITEM.route) {
                    SummaryScreen(doOnNavigationItemSelected)
                    BackHandler {
                        navController.navigate(HOME_ITEM.route) {
                            popUpTo(HOME_ITEM.route) { inclusive = false }
                        }
                    }
                }
            }
        }
    }
}