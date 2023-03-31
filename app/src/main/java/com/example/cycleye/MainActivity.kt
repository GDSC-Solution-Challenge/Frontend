package com.example.cycleye

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cycleye.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mainBinding : ActivityMainBinding? = null
    private val binding get() = mainBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        val emoji = 0x267B
        val emojiText = "${String(Character.toChars(emoji))}"

        setContentView(binding.root)
    }

    fun onMyButtonClick(v: View) {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        mainBinding = null
        super.onDestroy()
    }
}