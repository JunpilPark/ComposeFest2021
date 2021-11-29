package com.fest.compose.ui.customlayout

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch


@Composable
fun SimpleList() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
            Text("item $it")
        }
    }
}

@Composable
fun SimpleImageList() {
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = {
            coroutineScope.launch {
                scrollState.animateScrollToItem(0)
            }
        }) {
            Text("Scroll on top")
        }
        Button(onClick = {
            coroutineScope.launch {
                scrollState.animateScrollToItem(listSize - 1)
            }
        }) {
            Text("Scroll on end")
        }
    }
    LazyColumn(state = scrollState) {
        items(listSize) {
            ImageItems(index = it)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageItems(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text("item $index")
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints) // Measure Composable
        Log.d("firstBaselineTop", "placeable.width: ${placeable.width.toString()}")
        Log.d("firstBaselineTop", "placeable.height: ${placeable.height.toString()}")
        Log.d("firstBaselineTop", "placeable.measuredWidth: ${placeable.measuredWidth.toString()}")
        Log.d("firstBaselineTop", "placeable.measuredHeight: ${placeable.measuredHeight.toString()}")
        Log.d("constraints", "constraints.maxWidth: ${constraints.maxWidth.toString()}")
        Log.d("constraints", "constraints.maxHeight: ${constraints.maxHeight.toString()}")
        Log.d("constraints", "constraints.minWidth: ${constraints.minWidth.toString()}")
        Log.d("constraints", "constraints.minHeight: ${constraints.minHeight.toString()}")
        Log.d("constraints", "constraints.hasBoundedWidth: ${constraints.hasBoundedWidth.toString()}")
        Log.d("constraints", "constraints.hasBoundedHeight: ${constraints.hasBoundedHeight.toString()}")
        Log.d("constraints", "constraints.hasFixedWidth: ${constraints.hasFixedWidth.toString()}")
        Log.d("constraints", "constraints.hasFixedHeight: ${constraints.hasFixedHeight.toString()}")
        //https://kotlinlang.org/docs/operator-overloading.html#indexed-access-operator
        // 아래 placeable[] 은 Measurable 의 operator fun get(alignmentLine: AlignmentLine): Int
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]
        Log.d("constraints", "firstBaseline: $firstBaseline")
        Log.d("constraints", "roundToPx: ${firstBaselineToTop.roundToPx()}")
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        Log.d("constraints", "placeableY: $placeableY")
        val height = placeable.height + placeableY
        layout(placeable.width, height = height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    // custom layout attributes
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // 주어진 제약조건 들로 Child 를 측정하고, 배치
        val placeables = measurables.map {
                measurable -> measurable.measure(constraints)
        } // Measure

        var yPosition = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)
                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            CodelabTopAppBar()
        },
        bottomBar = {
            BottomAppBar {

            }
        }
    ) { innerPadding ->
        Log.d("padding", innerPadding.toString())
        Body(modifier = Modifier
            .padding(innerPadding)
            .padding(8.0.dp)
        )

    }
}

@Composable
fun CodelabTopAppBar() {
    TopAppBar(
        title = {
            Text("LayoutCodelab")
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            }
        }
    )
}

@Composable
fun Body(modifier: Modifier = Modifier) {
    /*   Column(modifier = modifier) {
           Text(text = "Hi there!")
           Text(text = "Thanks for going through the Layouts codelab")
           SimpleImageList()
       }*/
    MyOwnColumn(modifier = Modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("place items")
        Text("vertically")
        Text("we've done it by hand!!")
    }
}
