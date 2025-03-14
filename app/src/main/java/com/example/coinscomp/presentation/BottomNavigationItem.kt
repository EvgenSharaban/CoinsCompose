package com.example.coinscomp.presentation

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.coinscomp.R

data class BottomNavigationItem(
    @IdRes val idRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val titleRes: Int
)

val bottomNavigationItemsList = listOf(
    BottomNavigationItem(
        idRes = R.id.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        titleRes = R.string.home
    ),
    BottomNavigationItem(
        idRes = R.id.nav_summary,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        titleRes = R.string.summary
    )
)
