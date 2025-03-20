package com.example.coinscomp.presentation.uiviews.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.coinscomp.R
import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.uiviews.RoundImage
import com.example.coinscomp.ui.theme.CoinsCompTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCoin(
    item: ModelCoinsCustomView,
    onCoinClicked: () -> Unit,
    onCoinLongClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var localIsExpanded by remember { mutableStateOf(item.isExpanded) }

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        // Rhombus with rank
        Box(
            modifier = Modifier
                .padding(start = 32.dp)
                .align(Alignment.TopStart)
                .size(72.dp)
                .zIndex(1f)
        ) {
            CustomRhombus(
                text = item.rank.toString(),
                textSize = 24.sp,
                backColor = Color(0xFFFFA500), // remove static color
                cornerRadius = 16f,
                modifier = Modifier.fillMaxSize()
            )
        }

        // general card
        Card(
            modifier = Modifier
                .padding(top = 36.dp)
                .zIndex(0f),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            localIsExpanded = !localIsExpanded
                            onCoinClicked()
                        },
                        onLongClick = {
                            onCoinLongClicked()
                        }
                    )
            ) {
                // Name and price
                Row(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        fontSize = 30.sp,
                        lineHeight = 1.2.em
                    )
                    Text(
                        text = stringResource(R.string.price_for_coin, item.price),
                        fontSize = 18.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                // icon with description
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    RoundImage(
                        logo = item.logo,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(56.dp)
                    )
                    if (localIsExpanded) {
                        Text(
                            text = item.description,
                            textAlign = TextAlign.End,
                            fontSize = 16.sp
                        )
                    }
                }
                // creation date + rhombus with short name
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.creationDate,
                        fontSize = 14.sp
                    )
                    CustomRhombus(
                        text = item.shortName,
                        backColor = Color.White,
                        cornerRadius = 8f,
                        textColor = Color.Black,
                        textSize = 16.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(width = 64.dp, height = 56.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomCoinPreview() {
    CoinsCompTheme {
        CustomCoin(
            item = ModelCoinsCustomView(
                rank = 5,
                name = "Bitcoin Fake",
                price = 933532.32,
                description = "skjd skjahg hjf hjkkgfg hsdh skjd skjahg hjf hjkkgfg hsdh kkgfgdjghksjdhgksdhkhkgshkgshkh hsghkhk hkjhsgk hkshksghkhgkjhkjsgh sghk khkdhgjkshk  hsdh skjhgh sghskghskjd skjahg hjf hjkkgfg hsdh skjhgh sghskgh",
                creationDate = "Since 2009.12.12",
                shortName = "BTC",
                logo = "",
                id = "ew",
                isActive = true,
                type = "coin"
            ),
            onCoinClicked = {},
            onCoinLongClicked = {},
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}