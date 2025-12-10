package com.example.dice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import com.airbnb.lottie.compose.*
import com.example.dice.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun OneDieScreen(
    onRollComplete: (Int) -> Unit = {}
){
    var currentDiceValue by remember { mutableStateOf(Random.nextInt(1,7)) } //state to track current dice face
    var isRolling by remember { mutableStateOf(false) } //state to track if animation is playing
    var showAnimation by remember { mutableStateOf(false) } //state to show the animation
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.dice_roll)
    ) //lottie composition

    //animation state
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = showAnimation, //callback to the state that handles the playing of the animation
        restartOnPlay = true, //to play over and over again
        iterations = 1
    )

    //handle what happens when the user presses the roll dice button
    //@Composable

    LaunchedEffect(isRolling) {
        if (isRolling){
            delay(3000)
            val finalValue = Random.nextInt(1,7)
            currentDiceValue = finalValue

        //after displaying the final value stop the animation
            showAnimation = false
            isRolling = false
            onRollComplete(finalValue)
        }
    }

    fun rollDice(){
        if (isRolling) return
         isRolling = true
        showAnimation = true

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(250.dp),
                contentAlignment = Alignment.Center
            ) {
                if (showAnimation) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = getDiceDrawable(currentDiceValue)),
                        contentDescription = "Dice showing $currentDiceValue",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            Button(
                onClick = { rollDice() },
                enabled = !isRolling,
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (isRolling) "Rolling..." else "Roll",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

fun getDiceDrawable(value: Int): Int{
    return when (value){
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
}

@Preview(showBackground = true)
@Composable
fun OneDieScreenPreview(){
    MaterialTheme {
        OneDieScreen()
    }
}