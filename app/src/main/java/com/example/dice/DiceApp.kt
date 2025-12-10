package com.example.dice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dice.data.HistoryManager
import com.example.dice.ui.screens.OneDieScreen
import com.example.dice.ui.screens.SelectionScreen
import com.example.dice.ui.screens.TwoDiceScreen

//will hold navigation because i dont want the app bar showing in the selection screen

sealed class Screen(val route: String){
    object Select : Screen("select")
    object OneDie: Screen("one_die")
    object TwoDice: Screen("two_dice")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceApp(){
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val historyManager = remember { HistoryManager() }

    var showHistory by remember { mutableStateOf(false) }

    val showTopBar = currentRoute != Screen.Select.route

    Scaffold(
        topBar = {
            if (showTopBar){
                TopAppBar(
                    title = {
                        Text(
                            text = if (currentRoute == Screen.OneDie.route){
                                stringResource(R.string.one_die)
                            } else {
                                stringResource(R.string.two_dice)
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {navController.navigateUp()}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {showHistory = !showHistory}) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_history),
                                contentDescription = stringResource(R.string.history)
                            )
                        }
                        DropdownMenu(
                            expanded = showHistory,
                            onDismissRequest = {showHistory = false}
                        ) {
                            if (historyManager.isEmpty()){
                                DropdownMenuItem(
                                    text = {Text(stringResource(R.string.no_history_yet))},
                                    onClick = {showHistory = false},
                                    enabled = false
                                )
                            } else{
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Clear History",
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    onClick = {
                                        historyManager.clearHistory()
                                        showHistory = false
                                    }
                                )
                                HorizontalDivider()
                                historyManager.history.forEach {
                                    roll ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ){
                                                Text(roll.getDisplayText())
                                                Text(
                                                    roll.getTime(),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha= 0.5f)
                                                )
                                            }
                                        },
                                        onClick = {}
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    ) {
        paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Select.route,
            modifier = Modifier.padding(paddingValues)
        ){
            //selection screen
            composable(Screen.Select.route) {
                SelectionScreen(
                    onOneDiceSelected = {navController.navigate(Screen.OneDie.route)},
                    onTwoDiceSelected = {navController.navigate(Screen.TwoDice.route)}
                )
            }

            //single die screen (w/placeholder)
            composable (Screen.OneDie.route){
                OneDieScreen(
                    onRollComplete = {
                        value -> historyManager.addOneDieRoll(value)
                    }
                )
            }

            //two dice screen (w/placeholder)
            composable (Screen.TwoDice.route){
                TwoDiceScreen(
                    onRollComplete = {
                        dice1, dice2 -> historyManager.addTwoDiceRoll(dice1, dice2)
                    }
                )
            }
        }
    }
}