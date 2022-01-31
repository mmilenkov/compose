package org.selostudios.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowUI()
        }
    }
}
@Composable
fun ShowUI() {

    //SampleColumn()
    /*val painter = painterResource(id = R.drawable.ic_kotlin)
    Box(modifier = Modifier.fillMaxSize(0.5f)) {
        ImageCard(
            painter = painter,
            contentDescription = "Kotlin Icon",
            title = "Kotlin Icon"
        )
    }
     */
    //TextModifiers(text = "This is some random text to test with")
    /*val color = remember { //Remembers value from last recomposition. External state
        mutableStateOf(Color.Yellow)
    }
    Column() {
        StateTest(
            Modifier.weight(1f).fillMaxSize()
        ) {
            color.value = it
        }
        Box(modifier = Modifier.weight(2f).background(color.value).fillMaxSize())
    }
     */
    //SnackBarTest()
    //ListTest()
}

@Composable
fun ListTest() {
    //For a low number of items use Column (under 5)
   /* val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(state = scrollState)) {
        for (i in 1..50) {
            Text(
                "Item: $i",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }*/
    //Equivalent to RecyclerView. For above 5 items. Scrollable by default
    LazyColumn {
        //itemsIndexed() // Essentually a for each loop
        itemsIndexed(listOf("This","is","a","sample")) { index, string ->
            Text(
                "Item: $index Text: $string",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        /*items(5000) { //it = index
            Text(
                "Item: $it",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }*/

    }
}

@Composable
fun SnackBarTest() {
    // This allows full control for it. Permanently displayed
    Snackbar() {
        Text(text = "SnackbarText")
    }
    //Handles already existing material design components
    val scaffoldState = rememberScaffoldState()
    var textFieldState by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            TextField(value = textFieldState, label = {
                Text(text = "Enter your name")
            },
            onValueChange = { textFieldState = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Hello $textFieldState")
                }
            }) {
                Text("Please greet me!")
            }
        }
    }
}

@Composable
fun StateTest(modifier: Modifier = Modifier, updateColor: (Color) -> Unit) {
    val color = remember { //Remembers value from last recomposition. Internal state
        mutableStateOf(Color.Yellow)
    }
    Box(modifier = modifier
        .background(Color.Red)
        .clickable {
            updateColor(
                Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f
                )
            )

        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun TextModifiers(text: String) {
    val fontFamily = FontFamily(Font(R.font.comforter_regular, FontWeight.Normal))
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF101010))) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Red,
                        fontSize = 25.sp
                    )
                ) {
                    append("Hello, is it me you're looking for?")
                    append(" ")
                }
                append(text)
                withStyle(
                    style = SpanStyle(
                        color = Color.Red,
                        fontSize = 25.sp
                    )
                ) {
                    append(" ")
                    append("I can see it in your eyes")
                }
            },
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = fontFamily,
            textDecoration = TextDecoration.Underline,

        )
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 2.dp
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
            contentAlignment = Alignment.BottomStart
            ) {
                Text(text = title, style = TextStyle(color = Color.White, fontSize = 16.sp))
            }

        }
    }
}

@Composable
fun SampleColumn() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(2.dp)
        .border(1.dp, Color.Black)
        .padding(2.dp)
        .border(1.dp, Color.Red)
        .padding(2.dp)
        .border(1.dp, Color.Green)
        .padding(2.dp)
        .border(1.dp, Color.Blue)
        .background(Color.LightGray, RectangleShape),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Greeting("Android")
        Spacer(Modifier.height(30.dp))
        Greeting("Kotlin")
        Text(text = "Hello Compose", modifier = Modifier.offset(20.dp, 10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShowUI()
}