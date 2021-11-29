package com.fest.compose.ui.staggered

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.*


@Stable
fun Modifier.padding(all: Dp) =
    this.then(
        PaddingModifier(start = all, top = all, end = all, bottom = all, rtlAware = true)
    )

private class PaddingModifier(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
    val rtlAware: Boolean
): LayoutModifier {

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        Log.d("PaddingModifier", "state: $start")
        Log.d("PaddingModifier", "top: $top")
        Log.d("PaddingModifier", "end: $end")
        Log.d("PaddingModifier", "bottom: $bottom")
        val horizontal = start.roundToPx() + end.roundToPx()
        val vertical = top.roundToPx() + bottom.roundToPx()

        Log.d("PaddingModifier", "horizontal: $horizontal")
        Log.d("PaddingModifier", "vertical: $vertical")
        Log.d("PaddingModifier", "constraints offset 전")
        Log.d("PaddingModifier", "constraints max : ${constraints.maxWidth}, ${constraints.maxHeight}")
        Log.d("PaddingModifier", "constraints min : ${constraints.minWidth}, ${constraints.minHeight}")
        val placeable = measurable.measure(constraints = constraints.offset(- horizontal, - vertical))
        Log.d("PaddingModifier", "constraints offset 후")
        Log.d("PaddingModifier", "constraints max : ${constraints.maxWidth}, ${constraints.maxHeight}")
        Log.d("PaddingModifier", "constraints min : ${constraints.minWidth}, ${constraints.minHeight}")

        //너비를 가져와 제약 조건을 충족하는 가장 가까운 크기를 반환합니다.
        val width = constraints.constrainWidth(placeable.width + horizontal)
        //높이를 가져와 제약 조건을 충족하는 가장 가까운 크기를 반환합니다.
        val height = constraints.constrainHeight(placeable.height + vertical)

        return layout(width = width, height = height) {
            if (rtlAware) {
                placeable.placeRelative(start.roundToPx(), top.roundToPx())
            } else {
                placeable.place(start.roundToPx(), top.roundToPx())
            }
        }
    }
}