package com.example.coinscomp.presentation.uiviews.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.coinscomp.R
import com.example.coinscomp.presentation.coins.models.coins.ModelCoinsCustomView
import com.example.coinscomp.presentation.uiviews.RoundImage
import com.example.coinscomp.ui.theme.CoinsCompTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCoinConstraint(
    item: ModelCoinsCustomView,
    onCoinClicked: () -> Unit,
    onCoinLongClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var localIsExpanded by remember { mutableStateOf(item.isExpanded) }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        val (rankRef, containerRef) = createRefs()

        CustomRhombus(
            text = item.rank.toString(),
            textSize = 24.sp,
            backColor = Color(0xFFFFA500), // remove static color
            cornerRadius = 16f,
            modifier = Modifier
                .constrainAs(rankRef) {
                    start.linkTo(parent.start, margin = 32.dp)
                    top.linkTo(parent.top)
                }
                .size(72.dp)
                .zIndex(1f)
        )

        Card(
            modifier = Modifier
                .constrainAs(containerRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(rankRef.top, margin = 36.dp)
                    bottom.linkTo(parent.bottom)
                }
                .zIndex(0f),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
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
                    .padding(12.dp)
            ) {
                val (nameRef, priceRef, logoRef, descriptionRef, dateRef, shortNameRef) = createRefs()

                Text(
                    text = item.name,
                    fontSize = 30.sp,
                    lineHeight = 1.2.em,
                    modifier = Modifier
                        .constrainAs(nameRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top, margin = 32.dp)
                            end.linkTo(priceRef.start)
                        }
                )

                Text(
                    text = stringResource(R.string.price_for_coin, item.price),
                    fontSize = 18.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .constrainAs(priceRef) {
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.top)
                            bottom.linkTo(nameRef.bottom)
                            start.linkTo(nameRef.end, margin = 8.dp)
                            width = Dimension.fillToConstraints
                        }
                )

                RoundImage(
                    logo = item.logo,
                    modifier = Modifier
                        .size(56.dp)
                        .constrainAs(logoRef) {
                            start.linkTo(parent.start, margin = 20.dp)
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.bottom, margin = 8.dp)
                            horizontalBias = 0f
                        }
                )

                if (item.isExpanded) {
                    Text(
                        text = item.description,
                        textAlign = TextAlign.End,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .constrainAs(descriptionRef) {
                                start.linkTo(logoRef.end, margin = 16.dp)
                                top.linkTo(priceRef.bottom, margin = 8.dp)
                                bottom.linkTo(shortNameRef.top, margin = 8.dp)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                    )
                }

                Text(
                    text = item.creationDate,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .constrainAs(dateRef) {
                            start.linkTo(parent.start)
                            top.linkTo(logoRef.bottom, margin = 16.dp)
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(shortNameRef.start)
                            verticalBias = 1f
                            horizontalBias = 0f
                        }
                )

                CustomRhombus(
                    text = item.shortName,
                    backColor = Color.White,
                    cornerRadius = 8f,
                    textColor = Color.Black,
                    textSize = 16.sp,
                    modifier = Modifier
                        .constrainAs(shortNameRef) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(64.dp, 56.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomCoinConstraintPreview() {
    CoinsCompTheme {
        CustomCoinConstraint(
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