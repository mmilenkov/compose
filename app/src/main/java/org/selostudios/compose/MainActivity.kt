package org.selostudios.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
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
import androidx.compose.ui.layout.layoutId
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
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.selostudios.compose.ui.HomeScreen
import org.selostudios.compose.ui.theme.RelaxationAppUiTheme
import kotlin.random.Random

var i = 0

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RelaxationAppUiTheme() {
                HomeScreen()
            }
        }
    }
}

//Random Samples
//region
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
    //ConstraintLayout()
    //SimpleAnimations()
}

@Composable
fun SimpleAnimations() {
    var sizeState by remember {
        mutableStateOf(200.dp)
    }
    //Interpolates between size for animation
    val size by animateDpAsState(
        targetValue = sizeState,
        animationSpec = keyframes { //Large control
            durationMillis = 5000
            sizeState at 0 with LinearEasing
            sizeState * 1.5f at 1000 with FastOutLinearInEasing
            sizeState * 2f at 5000  with LinearOutSlowInEasing
        }

        /*spring( //Bouncy animation
            Spring.DampingRatioMediumBouncy
        )*/

        /*tween(
            delayMillis = 1000,
            durationMillis = 2000,
            easing = LinearOutSlowInEasing
        )//Animation curve*/
    )

    //infinite animation
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.DarkGray,
        animationSpec = infiniteRepeatable(
            animation = tween(2000,500, FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(size)
            .background(color),
    contentAlignment = Alignment.Center) {
        Button(onClick = {
            sizeState += 50.dp
        }) {
            Text("++")
        }
    }
}

@Composable
fun SideEffects(backDispatcher: OnBackPressedDispatcher) {
    // sideEffect - executed after every recomposition of our composable
    i++ // This is a sideEffect
    SideEffect { // called when a composable is recomposed
        i++
    }

    val callback = remember {
        object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Do something
            }

        }
    }
    //Added on every recomposition, but not removed. Memory leak
    //Use the below instead
    //backDispatcher.addCallback(callback)

    //This is used to clear after a composable is destroyed
    DisposableEffect(key1 = backDispatcher) {
        backDispatcher.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}

@Composable
fun SideEffects2() {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        var counter by remember {
            mutableStateOf(0)
        }

        if(counter % 5 == 0 && counter > 0) {
            //This effect cancels a coroutine and restarts it
                LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar("Snackbar")
                }
        }
        Button(onClick = { counter++ }) {
            Text("Clicked $counter times")
        }
    }
}

@Composable
fun SideEffects3() {
    // Based on LaunchedEffect. Executes async code inside coroutine
    // Once it is finished it will parse the result into state and trigger
    // composable to recompose. E.g network call
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        //Immutable.
        val counter = produceState(initialValue = 0) {
            delay(3000L)
            value = 4
        }

        if(counter.value % 5 == 0 && counter.value > 0) {
            //This effect cancels a coroutine and restarts it
            LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
                scaffoldState.snackbarHostState.showSnackbar("Snackbar")
            }
        }
        Button(onClick = {  }) {
            Text("Clicked ${counter.value} times")
        }
    }

}

@Composable
fun ConstraintLayout() {
    val constraints = ConstraintSet {
        val boxOne = createRefFor("boxOne") //Creates reference for composable in constraint
        val boxTwo = createRefFor("boxTwo")
        val guideline = createGuidelineFromTop(200.dp)
        //val barrier = createTopBarrier()

        constrain(boxOne) {
            top.linkTo(parent.top) // top constraint
            start.linkTo(parent.start) // start constraint
            end.linkTo(parent.end)
            width = Dimension.matchParent // comes from constraint layout
            height = Dimension.value(200.dp)
        }

        constrain(boxTwo) {
            top.linkTo(boxOne.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.matchParent
            height = Dimension.value(200.dp)
        }
    }
    androidx.constraintlayout.compose.ConstraintLayout(
        constraints,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .background(Color.Red)
            .layoutId("boxOne"))
        Box(modifier = Modifier
            .background(Color.Yellow)
            .layoutId("boxTwo"))
    }

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
//endregion

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShowUI()
}