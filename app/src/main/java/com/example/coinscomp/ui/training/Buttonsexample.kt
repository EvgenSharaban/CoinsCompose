package com.example.coinscomp.ui.training

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.coinscomp.ui.theme.CoinsCompTheme

@Composable
fun ButtonsExample(modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = "Click me")
    }

    Text(text = "Some text", textAlign = TextAlign.Center)
}

@Preview
@Composable
private fun ButtonsExamplePreview() {
    CoinsCompTheme {
        ButtonsExample()
    }
}