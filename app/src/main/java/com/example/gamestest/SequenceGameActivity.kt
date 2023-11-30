package com.example.gamestest

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.security.AccessController.getContext
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.math.floor

class SequenceGameActivity : AppCompatActivity() {
    var pattern: MutableList<Int> = mutableListOf<Int>()
    var gameLevel: Int = 0
    var gamePlaying: Boolean = false
    var cluePlaying: Boolean = false
    var clueHoldTime: Int = 1000
    var cluePauseTime: Int = 333
    var nextClueWaitTime: Int = 1000
    var guessCount: Int = 0

    lateinit var startGameButton: Button
    lateinit var llContent: LinearLayout

    var newButtonListener = View.OnClickListener {
        Log.d("id", "button id = ${it.id.toString()}")
        guess(it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sequence_game_activity)

        startGameButton = findViewById(R.id.start_game_button)
        llContent = findViewById(R.id.sequence_game_content)

        startGameButton.setOnClickListener {
            startGameButton.visibility = View.INVISIBLE
            createGrid()
            createPattern()
            startGame()
        }
    }

    fun startGame() {
        Log.d("pattern", "pattern created = $pattern")
        gamePlaying = true

        playClueSequence()
    }

    fun playClueSequence() {
        if (clueHoldTime >= 500)
            clueHoldTime -= 100

        Log.e("progress","gamelevel = $gameLevel")

        cluePlaying = true
        var delay: Long = nextClueWaitTime.toLong()
        for (i in 0..gameLevel) {
            Timer().schedule(delay) {
                val button: Button = findViewById(pattern[i])
                button.backgroundTintList = ContextCompat.getColorStateList(button.context, R.color.white)
                Timer().schedule(clueHoldTime.toLong()) {
                    button.backgroundTintList = ContextCompat.getColorStateList(button.context, R.color.light_blue)
                }
            }
            delay += clueHoldTime
            delay += cluePauseTime
        }
        cluePlaying = false
    }

    fun guess(buttonId: Int) {
        if (!gamePlaying || cluePlaying)
            return
        if (buttonId != pattern[guessCount]) {
            gameLoss()
            return
        }

        Log.v("guess and level", "guess = $guessCount, level = $gameLevel")
        if (guessCount == gameLevel) {
            guessCount = 0
            gameLevel++
            playClueSequence()
        } else {
            guessCount++;
        }
    }

    // game lost function
    fun gameLoss() {
        Log.d("score", "Your score! ${gameLevel.toString()}")
        gameLevel = 0
        guessCount = 0
        clueHoldTime = 1000
        cluePauseTime = 333
        nextClueWaitTime = 1000
        gamePlaying = false
        pattern.clear()
        llContent.removeAllViews()
        llContent.visibility = View.GONE
        startGameButton.visibility = View.VISIBLE
    }

    // function to create grid for game
    fun createGrid() {
        llContent.visibility = View.VISIBLE
        var buttonCount: Int = 0
        for (i in 1..3) {
            val llRow = LinearLayout(this)
            llRow.orientation = LinearLayout.HORIZONTAL
            llContent.addView(llRow)
            for (i in 1..3) {
                val newButton = Button(this)
                newButton.id = buttonCount
                newButton.width = 300
                newButton.height = 300
                newButton.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_blue)
                llRow.addView(newButton)
                buttonCount++
                newButton.setOnClickListener(newButtonListener)
            }
        }
    }

    // function to create sequence pattern
    fun createPattern() {
        for (i in 0..200) {
            pattern.add(floor(Math.random() * 8).toInt())
        }
    }
}