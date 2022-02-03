package org.selostudios.compose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.selostudios.compose.BottomMenuContent
import org.selostudios.compose.R
import org.selostudios.compose.easyQuadBezTo
import org.selostudios.compose.ui.theme.*

@ExperimentalFoundationApi
@Composable
fun HomeScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(DeepBlue)
    ) {
        Column {
            GreetingSection()
            BannerSection(banners = listOf("Sleep","Insomnia","Depression"))
            CurrentChoice()
            FeaturedSection(features = listOf(
                Feature(
                    title = "Sleep meditation",
                    R.drawable.ic_headphone,
                    BlueViolet1,
                    BlueViolet2,
                    BlueViolet3
                ),
                Feature(
                    title = "Tips for sleeping",
                    R.drawable.ic_videocam,
                    LightGreen1,
                    LightGreen2,
                    LightGreen3
                ),
                Feature(
                    title = "Night island",
                    R.drawable.ic_headphone,
                    OrangeYellow1,
                    OrangeYellow2,
                    OrangeYellow3
                ),
                Feature(
                    title = "Calming sounds",
                    R.drawable.ic_headphone,
                    Beige1,
                    Beige2,
                    Beige3
                )
            ))
        }
        BottomNav(
            navItems = listOf(
                BottomMenuContent("Home", R.drawable.ic_home),
                BottomMenuContent("Meditate", R.drawable.ic_bubble),
                BottomMenuContent("Sleep", R.drawable.ic_moon),
                BottomMenuContent("Music", R.drawable.ic_music),
                BottomMenuContent("Profile", R.drawable.ic_profile)
            ), modifier = Modifier.align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun GreetingSection(name: String = "John") {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Good morning, $name", style = MaterialTheme.typography.h2)
            Text(text = "We wish you have a good day!", style = MaterialTheme.typography.body1)
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Search Icon",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun BannerSection(banners: List<String>) {
    var selectedBanner by remember {
        mutableStateOf(0)
    }
    LazyRow {
        items(banners.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                    .clickable { selectedBanner = it }
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedBanner == it) {
                            ButtonBlue
                        } else {
                            DarkerButtonBlue
                        }
                    )
                    .padding(15.dp)
            ) {
                Text(text = banners[it], color = TextWhite)
            }
        }
    }
}

@Composable
fun CurrentChoice(color: Color = LightRed) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Thought",
                style = MaterialTheme.typography.h2
            )
            Text(
                text = "Meditation 3-10 min",
                style = MaterialTheme.typography.body1,
                color = TextWhite
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    ButtonBlue
                )
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Play Icon",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun FeaturedSection(features: List<Feature>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Features",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(15.dp)
        )
        LazyVerticalGrid(
            cells = GridCells.Fixed(2), //Entries per row
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(features.size) {
                FeatureItem(feature = features[it])
            }
        }
    }

}

@Composable
fun FeatureItem(feature: Feature) {
    BoxWithConstraints(modifier = Modifier
        .padding(7.5.dp)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(10.dp))
        .background(feature.darkColor)
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        val mediumColorPointStart = Offset(0f,height * 0.3f)
        val mediumColorPoint2 = Offset(width * 0.1f,height * 0.35f)
        val mediumColorPoint3 = Offset(width * 0.4f,height * 0.05f)
        val mediumColorPoint4 = Offset(width * 0.75f,height * 0.7f)
        val mediumColorPointEnd = Offset(width * 1.4f, -height.toFloat())
        
        val mediumColorPath = Path().apply { 
            //Math way. Not vector way
            moveTo(mediumColorPointStart.x, mediumColorPointStart.y)
            easyQuadBezTo(mediumColorPointStart, mediumColorPoint2)
            easyQuadBezTo(mediumColorPoint2, mediumColorPoint3)
            easyQuadBezTo(mediumColorPoint3, mediumColorPoint4)
            easyQuadBezTo(mediumColorPoint4, mediumColorPointEnd)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        val lightColorPointStart = Offset(0f,height * 0.35f)
        val lightColorPoint2 = Offset(width * 0.1f,height * 0.4f)
        val lightColorPoint3 = Offset(width * 0.3f,height * 0.05f)
        val lightColorPoint4 = Offset(width * 0.65f,height.toFloat())
        val lightColorPointEnd = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColorPath = Path().apply {
            //Math way. Not vector way
            moveTo(lightColorPointStart.x, lightColorPointStart.y)
            easyQuadBezTo(lightColorPointStart, lightColorPoint2)
            easyQuadBezTo(lightColorPoint2, lightColorPoint3)
            easyQuadBezTo(lightColorPoint3, lightColorPoint4)
            easyQuadBezTo(lightColorPoint4, lightColorPointEnd)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(path = mediumColorPath, color = feature.mediumColor)
            drawPath(path = lightColorPath, color = feature.lightColor)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Text(
                text = feature.title,
                style = MaterialTheme.typography.h2,
                lineHeight = 26.sp,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Icon(
                painter = painterResource(id = feature.iconId),
                contentDescription = feature.title,
                tint = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                text = "Start",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        //Handle click 
                    }
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ButtonBlue)
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )
        }
    }
}

@Composable
fun BottomNav(
    navItems: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    highlightColor: Color = ButtonBlue,
    textHighlightColor: Color = Color.White,
    inactiveTextColor: Color = AquaBlue,
    initialSelectedIndex: Int = 0
) {
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedIndex)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DeepBlue)
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, menuItem ->
            BottomMenuItem(
                item = menuItem,
                isSelected = index == selectedItemIndex,
                highlightColor = highlightColor,
                textHighlightColor = textHighlightColor,
                inactiveTextColor = inactiveTextColor
            ) {
                selectedItemIndex = index
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean,
    highlightColor: Color = ButtonBlue,
    textHighlightColor: Color = Color.White,
    inactiveTextColor: Color = AquaBlue,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onItemClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isSelected) {
                        highlightColor
                    } else {
                        Color.Transparent
                    }
                )
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if(isSelected) {
                    textHighlightColor
                } else {
                    inactiveTextColor
                },
                modifier = Modifier.size(20.dp)
            )
        }
        Text(text = item.title)
    }

}