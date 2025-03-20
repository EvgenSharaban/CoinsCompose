package com.example.coinscomp.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coinscomp.R
import kotlinx.serialization.Serializable

val bottomNavigationItemsList = listOf(
    NavigationItems.HOME_NAV_ITEM,
    NavigationItems.SUMMARY_NAV_ITEM
)

enum class NavigationItems(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleRes: Int
) {
    HOME_NAV_ITEM(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        titleRes = R.string.home
    ),
    SUMMARY_NAV_ITEM(
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        titleRes = R.string.summary
    )
}

sealed interface NavigationTypes {
    @Serializable
    object HomeType : NavigationTypes

    @Serializable
    object SummaryType : NavigationTypes
}