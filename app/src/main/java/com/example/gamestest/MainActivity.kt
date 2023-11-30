package com.example.gamestest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val game_sequence_button: Button = findViewById(R.id.game_sequence_button)
        val game_aim_trainer_button: Button = findViewById(R.id.game_aim_button)

        game_sequence_button.setOnClickListener {
            val intent = Intent(this, SequenceGameActivity::class.java)
            this.startActivity(intent)
        }
    }
}