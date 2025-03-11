package com.example.coinscomp.ui.training

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinscomp.ui.theme.CoinsCompTheme

@Composable
fun TrainingAppScreen(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(/*top = 48.dp, */bottom = 32.dp)
        ) {
            val list: List<String> = List(15) { "Buttons container" }
            item {
                Text(text = "Some simple title", textAlign = TextAlign.Center)
            }
            items(items = list) { item ->
                ContainerColumn(name = item) {
                    ButtonsExample()
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ExamplePreview() {
    CoinsCompTheme {
        TrainingAppScreen()
    }

}