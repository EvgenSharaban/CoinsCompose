package com.example.coinscomp.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coinscomp.R

val bottomNavigationItemsList = listOf(
    NavigationItems.HOME,
    NavigationItems.SUMMARY
)

enum class NavigationItems(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleRes: Int
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        titleRes = R.string.home
    ),
    SUMMARY(
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        titleRes = R.string.summary
    )
}