package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButtons()
    }

    private fun initButtons() {
        binding.buttonSave.setOnClickListener {
            val dayCompiler = binding.datePicker.dayOfMonth.toString() + "." +
                    binding.datePicker.month.toString() + "." +
                    binding.datePicker.year.toString()
            Toast.makeText(this, "$dayCompiler", Toast.LENGTH_SHORT).show()
            val eventModel: EventModel = EventModel(
                binding.editTextNameEvent.text.toString(),
                dayCompiler,
                binding.editTextDescriptionEvent.text.toString(),
                "F0000000"
            )
            val i = intent
            i.putExtra("Key", eventModel)
        }
    }
}