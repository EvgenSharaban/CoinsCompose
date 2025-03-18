package com.example.coinscomp.presentation.uiviews

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.coinscomp.R

@Composable
fun RoundImage(
    logo: String?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
    ) {
        AsyncImage(
            model = logo,
            placeholder = painterResource(R.drawable.case_detail_sample),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}