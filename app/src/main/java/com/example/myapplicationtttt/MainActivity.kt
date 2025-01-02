package com.example.myapplicationtttt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource 
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.myapplicationtttt.ui.theme.MyApplicationttttTheme
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationttttTheme {

                val listSize = 10
                val offsetXList = remember { List(listSize) { Animatable(0f) } }
                val offsetYList = remember { List(listSize) { Animatable(0f) } }
                val rotateList = remember { List(listSize) { Animatable(0f) } }

                val rotateY = remember { List(listSize) { Animatable(0f) } }
                val rotateYNoA = remember { List(listSize) { mutableFloatStateOf(0f) } }
                val zIndex = remember { List(listSize) { mutableFloatStateOf(0f) } }

                val text = remember { List(listSize) { mutableStateOf("") } }
                val message = remember { List(listSize) { mutableStateOf("") } }
                val isAnimation = remember { List(listSize) { mutableStateOf(false) } }

                val scope = rememberCoroutineScope()

                suspend fun allDeal(index: Int, callback: () -> Unit) {
                    coroutineScope {
                        zIndex[index].floatValue = (listSize - index).toFloat()
                        Log.i("wei test", "$index ${zIndex[index].floatValue}")

                        if (index >= listSize / 2) {
                            val aDeferred: Deferred<Unit> = async {
                                offsetXList[index].animateTo(
                                    targetValue = -(listSize * (40f - listSize * 1.8f)) - (index - listSize) * (100 - listSize * 2.5f),
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            val bDeferred: Deferred<Unit> = async {
                                offsetYList[index].animateTo(
                                    targetValue = 480f, animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            val cDeferred: Deferred<Unit> = async {
                                rotateList[index].animateTo(
                                    targetValue = 180f, animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            aDeferred.await()
                            bDeferred.await()
                            cDeferred.await()
                        } else {

                            val aDeferred: Deferred<Unit> = async {
                                offsetXList[index].animateTo(
                                    targetValue = -(listSize * (40f - listSize * 1.8f)) - (index + (listSize / 2) - listSize) * (100 - listSize * 2.5f),
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            val bDeferred: Deferred<Unit> = async {
                                offsetYList[index].animateTo(
                                    targetValue = -720f,
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            val cDeferred: Deferred<Unit> = async {
                                rotateList[index].animateTo(
                                    targetValue = 180f, animationSpec = tween(durationMillis = 1000)
                                )
                            }
                            aDeferred.await()
                            bDeferred.await()
                            cDeferred.await()
                        }

                        callback()
                    }

                }

                suspend fun allBack(index: Int) {
                    scope.launch {

                        zIndex[index].floatValue = 0f

                        val aDeferred: Deferred<Unit> = async {
                            offsetXList[index].animateTo(
                                targetValue = 0f, animationSpec = tween(durationMillis = 1000)
                            )
                        }
                        val bDeferred: Deferred<Unit> = async {
                            offsetYList[index].animateTo(
                                targetValue = 0f, animationSpec = tween(durationMillis = 1000)
                            )
                        }
                        val cDeferred: Deferred<Unit> = async {
                            rotateList[index].animateTo(
                                targetValue = 0f, animationSpec = tween(durationMillis = 1000)
                            )
                        }
                    }

                }


                fun callAction(index: Int) {
                    if (listSize > 0) {
                        scope.launch {
                            allDeal(index) {
                                if (0 < index) {
                                    callAction(index - 1)
                                }

                            }
                        }
                    }
                }

                fun callActionBack(index: Int) {
                    scope.launch {
                        for (i in 0 until listSize) {
                            allBack(i)
                        }
                    }
                }


                fun actionAllToBack() {
                    for (index in 0 until listSize) {
                        scope.launch {
                            if (isAnimation[index].value) {
                                return@launch
                            }
                            if (rotateY[index].value == 180f) {

                                var a = 180f
                                isAnimation[index].value = true
                                while (0 < a) {
                                    a -= 30f
                                    rotateY[index].animateTo(
                                        targetValue = a, animationSpec = tween(durationMillis = 1)
                                    )
                                    if (a <= 90) {
                                        rotateYNoA[index].floatValue = 0f
                                        message[index].value = "${text[index].value}背"
                                    }
                                }
                                isAnimation[index].value = false
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { it ->
                    print(it)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center // Center the content both horizontally and vertically
                    ) {
                        Column {
                            Box(
                                // Wrap the Text in a Box
                                modifier = Modifier
                                    .clickable {
                                        callAction(listSize - 1)
                                    }
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "我要發牌",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(Color.LightGray)
                                )
                            }
                            Box(
                                // Wrap the Text in a Box
                                modifier = Modifier
                                    .clickable {
                                        actionAllToBack()
                                        callActionBack(0)
                                    }
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "我要收牌",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(Color.LightGray)
                                )
                            }
                            Box(
                                // Wrap the Text in a Box
                                modifier = Modifier
                                    .clickable {
                                        actionAllToBack()
                                    }
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "全部回背",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(Color.LightGray)
                                )
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                for (i in 0 until listSize) {
                                    val offsetX = offsetXList[i]
                                    val offsetY = offsetYList[i]
                                    val rotate = rotateList[i]

                                    val rotateY = rotateY[i]
                                    val rotateYNoA = rotateYNoA[i]
                                    val message = message[i]
                                    val text = text[i]
                                    val isAnimation = isAnimation[i]
                                    val zIndex = zIndex[i]
                                    text.value = "發牌$i"
                                    message.value = "發牌${i}背"
                                    CardCompose(
                                        text.value,
                                        offsetX,
                                        offsetY,
                                        rotate,
                                        rotateY,
                                        rotateYNoA,
                                        message,
                                        isAnimation,
                                        zIndex
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CardCompose(
    text: String,
    offsetX: Animatable<Float, AnimationVector1D>,
    offsetY: Animatable<Float, AnimationVector1D>,
    rotate: Animatable<Float, AnimationVector1D>,
    rotateY: Animatable<Float, AnimationVector1D>,
    rotateYNoA: MutableFloatState,
    message: MutableState<String>,
    isAnimation: MutableState<Boolean>,
    zIndex: MutableState<Float>,
) {

    val scope = rememberCoroutineScope()

    suspend fun showOrHidePoker() {
        if (isAnimation.value) {
            return
        }
        if (rotateY.value == 0f) {
            var a = 0f
            isAnimation.value = true
            while (a < 180f) {
                a += 30f
                rotateY.animateTo(
                    targetValue = a, animationSpec = tween(durationMillis = 1)
                )
                if (a >= 90) {
                    rotateYNoA.floatValue = 180f
                    message.value = "${text}正"
                }
            }
            isAnimation.value = false
        } else {
            var a = 180f
            isAnimation.value = true
            while (0 < a) {
                a -= 30f
                rotateY.animateTo(
                    targetValue = a, animationSpec = tween(durationMillis = 1)
                )
                if (a <= 90) {
                    rotateYNoA.floatValue = 0f
                    message.value = "${text}背"
                }
            }
            isAnimation.value = false
        }

    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.zIndex(zIndex.value)

    ) {
        Box( // Wrap the Text in a Box
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .zIndex(zIndex.value)
                .offset { IntOffset(offsetX.value.toInt(), offsetY.value.toInt()) }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null
                ) {
                    scope.launch { showOrHidePoker() }
                }
                .graphicsLayer(
                    rotationY = rotateY.value, rotationZ = rotate.value
                )
                .border(
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                )
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .height(130.dp)
                .width(100.dp)

        ) {

            val painter: Painter = if (message.value.contains("正")) {
                painterResource(id = R.drawable.poker_svgrepo_a)
            } else {
                painterResource(id = R.drawable.ic_launcher_background)
            }
            Icon(
                painter = painter,
                contentDescription = "Local SVG",
                modifier = Modifier
                    .rotate(180f)
                    .graphicsLayer(
                        rotationY = rotateYNoA.floatValue
                    ),
                tint = Color.Red
            )
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationttttTheme {
        Greeting("Android")
    }
}