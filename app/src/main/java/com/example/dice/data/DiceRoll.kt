package com.example.dice.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class DiceRoll{
    abstract val timestamp: Long
    abstract fun getDisplayText(): String

    data class One(
        val value: Int,
        override val timestamp: Long = System.currentTimeMillis() //replace with the time of the system in that moment
    ): DiceRoll(){
        override fun getDisplayText(): String = value.toString() //take the integer and convert to string to display

    }

    data class Two(
        val dice1: Int,
        val dice2: Int,
        override val timestamp: Long = System.currentTimeMillis() //replace with the time of the system in that moment
    ): DiceRoll(){
        override fun getDisplayText(): String = "$dice1, $dice2" //take the integer and convert to string to display

        fun getTotal(): Int = dice1 + dice2
    }

    //format to display the timestamp
    fun getTime(): String{
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
