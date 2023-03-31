package com.example.cycleye

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cycleye.databinding.ActivityExplainBinding
import com.example.cycleye.databinding.ActivityStartBinding

class ExplainActivity : AppCompatActivity() {
    private var explainBinding : ActivityExplainBinding? = null
    private val binding get() = explainBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        explainBinding = ActivityExplainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onPaperBtnClick(v: View){
        val intent = Intent(this, PaperActivity::class.java)
        startActivity(intent)
    }

    fun onMetalBtnClick(v: View){
        val intent = Intent(this, MetalActivity::class.java)
        startActivity(intent)
    }

    fun onGlassBtnClick(v: View){
        val intent = Intent(this, GlassActivity::class.java)
        startActivity(intent)
    }

    fun onPlasticBtnClick(v: View){
        val intent = Intent(this, PlasticActivity::class.java)
        startActivity(intent)
    }

    fun onVinylBtnClick(v: View){
        val intent = Intent(this, VinylActivity::class.java)
        startActivity(intent)
    }

    fun onPolyBtnClick(v: View){
        val intent = Intent(this, PolyActivity::class.java)
        startActivity(intent)
    }

    fun onIcepackBtnClick(v: View){
        val intent = Intent(this, IcepackActivity::class.java)
        startActivity(intent)
    }

    fun onWoodBtnClick(v: View){
        val intent = Intent(this, WoodActivity::class.java)
        startActivity(intent)
    }

    fun onClothesBtnClick(v: View){
        val intent = Intent(this, ClothesActivity::class.java)
        startActivity(intent)
    }

    fun onElectronicBtnClick(v: View){
        val intent = Intent(this, ElectronicActivity::class.java)
        startActivity(intent)
    }

    fun onFurnitureBtnClick(v: View){
        val intent = Intent(this, FurnitureActivity::class.java)
        startActivity(intent)
    }

    fun onLightBtnClick(v: View){
        val intent = Intent(this, LightActivity::class.java)
        startActivity(intent)
    }

}