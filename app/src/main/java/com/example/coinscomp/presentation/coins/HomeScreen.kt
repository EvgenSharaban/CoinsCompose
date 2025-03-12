package com.example.coinscomp.presentation.coins

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinscomp.presentation.bottomNavigationItemsList
import com.example.coinscomp.ui.theme.CoinsCompTheme


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()

        ) {
            Box(Modifier
                .weight(1f)
                .fillMaxWidth()) {
                LazyColumn {

                }
                FloatingActionButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            NavigationBar(Modifier.fillMaxWidth()) {
                bottomNavigationItemsList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        label = {
                            Text(text = stringResource(item.titleRes))
                        },
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navController.navigate(item.title)
                        },
                        icon = {
//                            BadgedBox(
//                                badge = {
//                                    if (item.badgeCount != null) {
//                                        Badge {
//                                            Text(text = item.badgeCount.toString())
//                                        }
//                                    } else if (item.hasNews) {
//                                        Badge()
//                                    }
//                                }
//                            ) {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = stringResource(item.titleRes)
                            )
//                            }
                        }
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    CoinsCompTheme {
        HomeScreen()
    }
}
