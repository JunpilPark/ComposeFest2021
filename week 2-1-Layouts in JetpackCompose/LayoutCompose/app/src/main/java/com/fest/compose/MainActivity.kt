package com.fest.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast
import com.fest.compose.ui.constraint.LargeConstraintLayout
import com.fest.compose.ui.staggered.Chip
import com.fest.compose.ui.staggered.StaggeredGrid
import com.fest.compose.ui.theme.LayoutComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutComposeTheme {
                // A surface container using the 'background' color from the theme
                BodyContent()
            }
        }
    }
}

@Composable
fun BodyContent() {
    val topics = listOf(
        "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
        "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
        "Religion", "Social sciences", "Technology", "TV", "Writing"
    )

    Scaffold(
        topBar = {
        },
        bottomBar = {
        }
    ) { innerPadding ->
        Row(modifier = Modifier
            .background(Color.LightGray)
            .padding(16.dp)
            .size(200.dp)
            .horizontalScroll(rememberScrollState())) {
            StaggeredGrid(
                rows = 5
            ) {
                for (topic in topics) {
                    Chip(modifier = Modifier.padding(8.dp), text = topic)
                }
            }
        }

    }
}

@Composable
fun TwoText(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),

            text = text2
        )
    }
}

@Preview
@Composable
fun LayoutsCodelabPreview() {
    LayoutComposeTheme() {
        Surface() {
            TwoText(text1 = "Hi!!", text2 = "There")
        }
    }
}
