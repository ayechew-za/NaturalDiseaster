package com.example.disease.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.disease.data.repo.AnnouncementRepository
import com.example.disease.navigation.Screen
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun Home(
    onAnnounceClick: () -> Unit,
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val repository = AnnouncementRepository(
        com.example.disease.data.network.RetrofitClient.apiService
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            NavigationDrawer(
                repository = repository,
                onItemSelected = { categoryName, type ->
                    println("Selected: $categoryName, Type: $type")
                    // Use existing navigation route from HorizontalScroll
                    navController.navigate(
                        Screen.CategoryDetail.createRoute(
                            categoryId = "", // You might need to pass category ID instead of name
                            type = type ?: "post"
                        )
                    )
                    scope.launch {
                        drawerState.close()
                    }
                },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    )





    {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Dark blue box with smaller wave
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color(0xFF254365))
                ) {
                    // Top-centered text
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 55.dp, bottom = 25.dp, start = 6.dp)
                                .fillMaxWidth()
                        ) {
                            // Menu Icon - Left (Clickable)
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                modifier = Modifier
                                    .size(24.dp, 30.dp)
                                    .align(Alignment.CenterStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu Icon",
                                    tint = Color.White
                                )
                            }

                            // Text - Center
                            Text(
                                text = "Hello\n okfsv",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                ),
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        // Announcement Clickable Box
                        Box(
                            modifier = Modifier
                                .width(296.dp)
                                .height(38.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFECFDF2))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF3AB77C),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    onAnnounceClick()
                                }
                        ) {
                            Row(modifier = Modifier.padding(5.dp)) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data("file:///android_asset/megaphone1.svg")
                                        .decoderFactory(SvgDecoder.Factory())
                                        .build(),
                                    contentDescription = "Megaphone",
                                    modifier = Modifier.size(24.dp, 30.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "မြန်မာနိုင်ငံလတ်တလောရာသီဥတုအခြေအနေ",
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    color = Color.Green,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Smaller wave at the bottom of dark blue section
                    WaveBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .align(Alignment.BottomCenter),
                        waveColor = Color(0xFFCBE1FA),
                        backgroundColor = Color(0xFF254365),
                        amplitude = 12f,
                        frequency = 2
                    )
                }

                // Light blue background with smaller wave at the top
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Smaller wave at the top of the light blue background
                    WaveBoxTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .align(Alignment.TopCenter),
                        waveColor = Color(0xFFCBE1FA),
                        backgroundColor = Color(0xFFCBE1FA),
                        amplitude = 12f,
                        frequency = 2
                    )

                    // Main content area
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                            .background(Color(0xFFCBE1FA))
                    ) {
                        HorizontalScroll(repository = repository, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun WaveBox(
    modifier: Modifier = Modifier,
    waveColor: Color = Color.White,
    backgroundColor: Color = Color(0xFF254365),
    amplitude: Float = 12f,
    frequency: Int = 2,
    animationDuration: Int = 2000,
    content: @Composable () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition()
    val phaseShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .background(backgroundColor)
            .drawBehind {
                val width = size.width
                val height = size.height

                val path = android.graphics.Path().apply {
                    moveTo(0f, 0f)

                    for (x in 0..width.toInt() step 2) {
                        val waveHeight = amplitude * sin(
                            (x.toFloat() / width) * frequency * 2 * PI.toFloat() + phaseShift
                        )

                        val y = height - (waveHeight + amplitude)

                        if (x == 0) {
                            moveTo(0f, y)
                        } else {
                            lineTo(x.toFloat(), y)
                        }
                    }

                    lineTo(width, height)
                    lineTo(0f, height)
                    lineTo(0f, 0f)
                    close()
                }

                drawPath(path.asComposePath(), waveColor)
            }
    ) {
        content()
    }
}

@Composable
fun WaveBoxTop(
    modifier: Modifier = Modifier,
    waveColor: Color = Color.White,
    backgroundColor: Color = Color(0xFFCBE1FA),
    amplitude: Float = 12f,
    frequency: Int = 2,
    animationDuration: Int = 2000,
    content: @Composable () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition()
    val phaseShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .background(backgroundColor)
            .drawBehind {
                val width = size.width
                val height = size.height

                val path = android.graphics.Path().apply {
                    moveTo(0f, height)

                    for (x in 0..width.toInt() step 2) {
                        val waveHeight = amplitude * sin(
                            (x.toFloat() / width) * frequency * 2 * PI.toFloat() + phaseShift
                        )

                        val y = waveHeight + amplitude

                        if (x == 0) {
                            moveTo(0f, y)
                        } else {
                            lineTo(x.toFloat(), y)
                        }
                    }

                    lineTo(width, 0f)
                    lineTo(0f, 0f)
                    lineTo(0f, height)
                    close()
                }

                drawPath(path.asComposePath(), waveColor)
            }
    ) {
        content()
    }
}