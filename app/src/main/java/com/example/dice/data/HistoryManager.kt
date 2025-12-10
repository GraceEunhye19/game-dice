package com.example.dice.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class HistoryManager{
    private val _history: SnapshotStateList<DiceRoll> = mutableStateListOf()

    val history: List<DiceRoll> get() = _history

    fun addOneDieRoll(value: Int){
        _history.add(0, DiceRoll.One(value))
    }

    fun addTwoDiceRoll(dice1: Int, dice2: Int){
        _history.add(0, DiceRoll.Two(dice1, dice2))
    }

    fun clearHistory(){_history.clear()}

    fun isEmpty(): Boolean = _history.isEmpty()
}