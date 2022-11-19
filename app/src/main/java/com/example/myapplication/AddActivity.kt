package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBinding
    var index = 0
    lateinit var colors : List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        colors = listOf<Int>(
            getColor(R.color.item_color_1),
            getColor(R.color.item_color_2),
            getColor(R.color.item_color_3),
            getColor(R.color.item_color_4),
            getColor(R.color.item_color_5),
            getColor(R.color.item_color_6)
        )
        initButtons()
    }

    private fun initButtons() {
        binding.buttonSave.setOnClickListener {
            val dayCompiler = binding.datePicker.dayOfMonth.toString() + "." +
                    binding.datePicker.month.toString() + "." +
                    binding.datePicker.year.toString()
            val eventModel: EventModel = EventModel(
                binding.editTextNameEvent.text.toString(),
                dayCompiler,
                binding.editTextDescriptionEvent.text.toString(),
                colors[index]
            )
            val i = intent
            i.putExtra("Key", eventModel)
            setResult(RESULT_OK, i)
            finish()
        }
        binding.buttonExit.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        binding.buttonColor.setOnClickListener {
            index++
            if (index == colors.size) index = 0
            Toast.makeText(this, "${colors[index]}", Toast.LENGTH_SHORT).show()
            binding.cardButtonColorBackground.setCardBackgroundColor(colors[index])
        }
    }
}