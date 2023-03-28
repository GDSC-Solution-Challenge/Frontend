package com.example.cycleye

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cycleye.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private var startBinding : ActivityStartBinding? = null
    private val binding get() = startBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBinding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onExplainBtnClick(v: View){
        val intent = Intent(this, ExplainActivity::class.java)
        startActivity(intent)
    }

    fun onPhotoBtnClick(v: View){
        val intent = Intent(this, PhotoActivity::class.java)
        startActivity(intent)
    }

    fun onRecordBtnClick(v: View){
        val intent = Intent(this, RecordActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        startBinding = null
        super.onDestroy()
    }
}