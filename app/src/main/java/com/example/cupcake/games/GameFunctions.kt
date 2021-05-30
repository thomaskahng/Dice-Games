package com.example.cupcake.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

//Responsible for data
class GameFunctions: ViewModel() {
    //Live data date, game, and played result
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _game = MutableLiveData<String>()
    val game: LiveData<String> = _game

    private val _won = MutableLiveData<Boolean>()
    val won: LiveData<Boolean> = _won

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    //Number for lucky number game
    private val _num = MutableLiveData<Int>()
    val num: LiveData<Int> = _num

    //Bet for double or nothing
    private val _bet = MutableLiveData<Boolean>()
    val bet: LiveData<Boolean> = _bet

    //For today's date
    val dateOptions = getToday()

    //Constructor resets values
    init {
        resetValues()
    }

    //Reset the values before playing
    fun resetValues() {
        _game.value = ""
        _won.value = false
        _result.value = ""
        _date.value = dateOptions[0]

        _num.value = 0
        _bet.value = false
    }

    //Set game played
    fun playGame(played: String) {
        _game.value = played
    }

    fun getPlayGame(): String? {
        return _game.value
    }

    //First roll set
    fun setRoll1(roll1: Int)  {
        _num.value = roll1
    }

    //Lucky number game play
    fun rollAgain(roll2: Int): Boolean {
        return (_num.value == roll2)
    }

    //Set bet (Double or Nothing and Yahtzee)
    fun setBet(bet: Boolean) {
        _bet.value = bet
    }

    //Get bet (Double or Nothing and Yahtzee)
    fun getGameBet(): Boolean? {
        return _bet.value
    }

    //Double or nothing game play
    fun doubleOrNothing(roll1: Int, roll2: Int): Boolean {
        return (roll1 == roll2)
    }

    //Yahtzee game play
    fun yahtzee(roll1: Int, roll2: Int, roll3: Int, roll4: Int, roll5: Int): Boolean {
        return (roll1 == roll2 && roll2 == roll3 && roll3 == roll4 && roll4 == roll5)
    }

    //Game won or not?
    fun winGame(result: Boolean) {
        _won.value = result
    }

    //Show result in pop up dialog
    fun gameResult(): Boolean? {
        return _won.value
    }

    //Result showed at end
    fun gamePlayResult(won: Boolean) {
        if (won == true)
            _result.value = "You Win!"
        else
            _result.value = "You Lose!"
    }

    fun getToday(): List<String> {
        //Mutable string list of pickup options
        val options = mutableListOf<String>()

        //Format a date, next line get a calendar date instance
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        //Add the current date on the calendar to options
        options.add(formatter.format(calendar.time))

        //Set date and return
        _date.value = options[0]
        return options
    }
}