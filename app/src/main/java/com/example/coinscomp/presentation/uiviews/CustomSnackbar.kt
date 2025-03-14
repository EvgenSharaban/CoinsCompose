package com.example.coinscomp.presentation.uiviews

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        action = {
            TextButton(
                onClick = { snackbarData.dismiss() }
            ) {
                Text(
                    text = snackbarData.visuals.actionLabel ?: "ОК",
                    color = Color.Gray
                )
            }
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = snackbarData.visuals.message,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}