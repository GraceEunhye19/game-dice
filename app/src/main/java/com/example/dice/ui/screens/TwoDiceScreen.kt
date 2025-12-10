package com.example.dice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dice.R
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun TwoDiceScreen(
    onRollComplete: (Int, Int) -> Unit = {_, _ ->}
){
    var dice1Value by remember { mutableStateOf(Random.nextInt(1,7)) }
    var dice2Value by remember { mutableStateOf(Random.nextInt(1,7)) }
    var isRolling by remember { mutableStateOf(false) }
    var showAnimation by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.dice_roll)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = showAnimation,
        restartOnPlay = true,
        iterations = 1
    )

    LaunchedEffect(isRolling) {
        if (isRolling){
            delay(3000)
            val finalDice1Value = Random.nextInt(1,7)
            val finalDice2Value = Random.nextInt(1,7)
            dice1Value = finalDice1Value
            dice2Value = finalDice2Value


            //after displaying the final value stop the animation
            showAnimation = false
            isRolling = false
            onRollComplete(finalDice1Value, finalDice2Value)

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
                    .size(350.dp),
                contentAlignment = Alignment.Center
            ){
                if (showAnimation){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier.size(180.dp)
                        )
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier.size(180.dp)
                        )
                    }
                }
                else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = getDiceDrawable(dice1Value)),
                            contentDescription = "Dice showing $dice1Value",
                            modifier = Modifier.size(150.dp)
                        )
                        Image(
                            painter = painterResource(id = getDiceDrawable(dice2Value)),
                            contentDescription = "Dice showing $dice2Value",
                            modifier = Modifier.size(150.dp)
                        )
                    }
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

@Preview(showBackground = true)
@Composable
fun TwoDiceScreenPreview(){
    MaterialTheme {
        TwoDiceScreen()
    }
}