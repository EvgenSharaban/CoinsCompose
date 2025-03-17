package com.example.coinscomp.presentation.uiviews.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.coinscomp.R
import com.example.coinscomp.ui.theme.CoinsCompTheme

@Composable
fun AddNoteAlertDialog(
    onDismiss: () -> Unit,
    onConfirmation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val enteredText = remember { mutableStateOf("") }
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.add_note))
        },
        text = {
            TextField(
                value = enteredText.value,
                onValueChange = {
                    enteredText.value = it
                },
                label = { Text(stringResource(R.string.enter_note)) }
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(enteredText.value)
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AddNoteAlertDialogPreview() {
    CoinsCompTheme {
        AddNoteAlertDialog(
            onDismiss = {},
            onConfirmation = {}
        )
    }
}