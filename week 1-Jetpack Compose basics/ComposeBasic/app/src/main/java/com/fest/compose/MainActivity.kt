package com.fest.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fest.compose.ui.theme.ComposeBasicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBasicTheme {
                MyApp()
            }
        }
    }
}

@Composable
private fun MyApp() {
    var shouldShowOnBoarding by rememberSaveable {
        mutableStateOf(true)
    }

    if(shouldShowOnBoarding) {
        OnBoardingScreen { shouldShowOnBoarding = false }
    }
    else {
        Greetings()
    }
}

@Composable
fun Greetings(names: List<String> = List(1000) {"$it"}) {
    LazyColumn( modifier = Modifier.padding(vertical = 4.0.dp) ) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Surface (
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Card(
            modifier = Modifier.shadow(2.dp)
        ) {
            CardContents(name)
        }
    }

}

@Composable
fun CardContents(name: String) {
    var expanded  by rememberSaveable {
        Log.d("Greeting", "call remember in Greeting!!!! $name")
        mutableStateOf(false)
    }

    Row(modifier = Modifier
        .padding(12.dp)
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(12.dp)
        ) {
            Text("Hello, ")
            Text(name, style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.ExtraBold
            ))
            if(expanded) {
                Text("확장 시 표시 될 TEXT")
            }
        }
        IconButton(
            onClick = { expanded.apply { expanded = !expanded } }
        ) {
            val contentDescription =
                if (expanded) {
                    stringResource(id = R.string.show_less)
                } else {
                    stringResource(id = R.string.show_more)
                }
            val icons = if(expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore
            Icon(imageVector = icons , contentDescription = contentDescription)
        }
    }
}

@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {

    Surface {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basic Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }

}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, widthDp = 320)
@Composable
fun GreetingsPreview() {
    ComposeBasicTheme {
       Greetings()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO, widthDp = 320)
@Composable
fun GreetingsPreviewLightMode() {
    GreetingsPreview()
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    ComposeBasicTheme {
        OnBoardingScreen {}
    }
}

