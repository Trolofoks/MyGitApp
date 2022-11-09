package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }
    private fun init(){
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.delete -> {}
                R.id.add -> {
                    startActivity(Intent(this,AddActivity::class.java))
                }
            }
            true
        }

    }
}