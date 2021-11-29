package com.fest.compose.ui.staggered

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout (
        modifier = modifier,
        content = content
    ) { measurables, constratints ->
        // mesurables 는 하위 View 의 개수가 Size 이다.

        // 각 행에 대한 너비들을 저장하기 위한 변수
        val rowWidths = IntArray(rows) { 0 }
        // 각 행에 대한 높이들을 저장하기 위한 변수
        val rowHeights = IntArray(rows) { 0 }

        // 하위의 view들을 제한하지 않고, 주어진 제약조건들과 함께 측정
        val placeables = measurables.mapIndexed { index, measurable ->
            Log.d("StaggeredGrid", "index: $index, measurable : $measurable")
            // 각 하위 요소를 측정
            val placeable =  measurable.measure(constratints)
            val row = index % rows
            Log.d("StaggeredGrid", "row: $row, placeable : $placeable")
            Log.d("StaggeredGrid", "placeable.width: ${placeable.width}")
            rowWidths[row] += placeable.width
            Log.d("StaggeredGrid", "rowWidths[row]: ${rowWidths[row]}")
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)
            Log.d("StaggeredGrid", "rowHeights[row]: ${rowHeights[row]}")
            placeable
        }

        // Grid의 너비는 가장 넓은 행
        val width = rowWidths.maxOrNull()?.coerceIn(constratints.minWidth.rangeTo(constratints.maxWidth)) ?: constratints.minWidth

        // Grid의 높이는 높이 제약조건으로 인해 강제로 변환된 각 행의 가장 높은 요소의 합이다.
        val height = rowHeights.sumOf{ it }.coerceIn(constratints.minHeight.rangeTo(constratints.maxHeight))

        val rowY = IntArray(rows) { 0 }
        for(i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        layout(width = width, height = height) {
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}
