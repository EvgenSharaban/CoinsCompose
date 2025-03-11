package com.example.coinscomp.ui.presentation.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinscomp.R
import com.example.coinscomp.ui.presentation.bottomNavigationItemsList
import com.example.coinscomp.ui.theme.CoinsCompTheme

@Composable
fun SummaryScreen(modifier: Modifier = Modifier) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()

        ) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.summary_statistics), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Text(text = stringResource(R.string.member_for_days, 10), modifier = Modifier.fillMaxWidth())
                Text(text = stringResource(R.string.total_items_count, 10), modifier = Modifier.fillMaxWidth())
                Text(text = stringResource(R.string.hidden_coins_count, 10), modifier = Modifier.fillMaxWidth())
                Text(text = stringResource(R.string.total_notes_count, 10), modifier = Modifier.fillMaxWidth())
                Text(text = stringResource(R.string.the_day_on_which_the_most_notes_were_taken, 10), modifier = Modifier.fillMaxWidth())
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
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
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
    }

}

@Preview(showSystemUi = true)
@Composable
private fun SummaryScreenPreview() {
    CoinsCompTheme {
        SummaryScreen()
    }
}