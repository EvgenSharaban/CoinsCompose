package com.example.coinscomp.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coinscomp.R
import com.example.coinscomp.presentation.utils.BottomNavigationConstants.HOME
import com.example.coinscomp.presentation.utils.BottomNavigationConstants.SUMMARY

val bottomNavigationItemsList = listOf(
    NavigationItems.HOME_ITEM,
    NavigationItems.SUMMARY_ITEM
)

enum class NavigationItems(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleRes: Int
) {
    HOME_ITEM(
        route = HOME,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        titleRes = R.string.home
    ),
    SUMMARY_ITEM(
        route = SUMMARY,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        titleRes = R.string.summary
    )
}

private object BottomNavigationConstants {
    const val HOME = "home"
    const val SUMMARY = "summary"
}