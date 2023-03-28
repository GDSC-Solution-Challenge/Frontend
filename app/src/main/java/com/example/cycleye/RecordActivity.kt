package com.example.cycleye

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.cycleye.databinding.ActivityRecordBinding
import java.io.File

class RecordActivity : AppCompatActivity() {
    private var recordBinding : ActivityRecordBinding? = null
    private val binding get() = recordBinding!!
    private val INTERNAL_FILE_PATH : String = "/data/data/com.example.cycleye/files/image"
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordBinding = ActivityRecordBinding.inflate(layoutInflater)
        val fList = getFileList()

        println("=================== ${fList.isEmpty()}")
        println("=================== ${fList.size}")

        val start = (page-1)*4
        val end = (page)*4

        val imgViewList = arrayListOf<ImageView>(binding.img1, binding.img2, binding.img3, binding.img4)

        for(i in start until end){
            val target = i%4
            if(i >= fList.size){
                imgViewList[target].setImageResource(0)
            }else{
                val img = BitmapFactory.decodeFile(fList[i].absolutePath)
                imgViewList[target].setImageBitmap(img)
            }
        }

        setContentView(binding.root)
    }

    private fun getFileList() : Array<File> {
        val dir = File(INTERNAL_FILE_PATH)
        val fList = dir.listFiles()

        if(fList.isNullOrEmpty()){
            return arrayOf()
        }

        println("=================================================")
        println("File Directory : $INTERNAL_FILE_PATH")
        fList.forEach { f: File ->
            println("FileName : ${f.name}")
        }
        println("=================================================")

        return fList
    }
}