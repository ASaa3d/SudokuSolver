package com.example.sudokusolver.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.wallpaper
import sudokusolver.shared.generated.resources.wallpaper2

private val imagesGrid: List<DrawableResource> = listOf(
    Res.drawable.wallpaper,
    Res.drawable.wallpaper2,
)

@Composable
fun ImageHorizontalGrid(
    modifier: Modifier = Modifier
) {
    val pageCount = imagesGrid.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        // 1. Horizontal Pager displaying one image per page
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight().align(Alignment.Center),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { pageIndex ->
            val drawableRes = imagesGrid[pageIndex]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = painterResource(drawableRes),
                    contentDescription = "Local Photo ${pageIndex + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        // 2. Page Indicator Dots
        Row(
            modifier = Modifier.wrapContentSize().align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                val size = if (pagerState.currentPage == iteration) 12.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(size)
                )
            }
        }
    }
}
