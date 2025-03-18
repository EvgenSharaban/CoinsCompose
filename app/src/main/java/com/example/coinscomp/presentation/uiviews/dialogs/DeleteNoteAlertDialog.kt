package com.example.coinscomp.presentation.uiviews.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.coinscomp.R
import com.example.coinscomp.ui.theme.CoinsCompTheme

@Composable
fun DeleteNoteAlertDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!openDialog) return
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.are_you_sure_you_want_delete_item),
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
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
private fun DeleteNoteAlertDialogPreview() {
    CoinsCompTheme {
        DeleteNoteAlertDialog(
            true,
            onDismiss = {},
            onConfirmation = {}
        )
    }
}