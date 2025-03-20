package com.example.coinscomp.presentation.uiviews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.coinscomp.presentation.utils.NavigationItems
import com.example.coinscomp.presentation.utils.bottomNavigationItemsList

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (NavigationItems) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedNavItemIndex by rememberSaveable { mutableIntStateOf(selectedIndex) }

    NavigationBar(modifier.fillMaxWidth()) {
        bottomNavigationItemsList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavItemIndex == index,
                onClick = {
                    if (selectedNavItemIndex != index) {
                        selectedNavItemIndex = index
                        onItemSelected(item)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedNavItemIndex) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = stringResource(item.titleRes)
                    )
                }
            )
        }
    }
}