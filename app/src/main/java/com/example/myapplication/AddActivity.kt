package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddBinding
    var index = 0
    lateinit var colors : List<Int>
    var item: EventModel? = null

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
        ifEdit()
    }

    private fun ifEdit() {
        val item : EventModel? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Constance.EDIT_INFO, EventModel::class.java)
        } else {
            intent.getSerializableExtra(Constance.EDIT_INFO) as EventModel?
        }

        if (item != null){
            binding.apply {
                editTextNameEvent.setText(item.eventType)
                val dataMiniArrayList = item.dataOfEvent.split(".")
                datePicker.init(dataMiniArrayList[2].toInt(),
                    dataMiniArrayList[1].toInt(),
                    dataMiniArrayList[0].toInt(),
                    null
                )
                editTextDescriptionEvent.setText(item.eventDescription)
            }
        }

    }

    private fun dayCompiler(): EventModel{
        val dayComp = binding.datePicker.dayOfMonth.toString() + "." +
                binding.datePicker.month.toString() + "." +
                binding.datePicker.year.toString()
        return EventModel(
            binding.editTextNameEvent.text.toString(),
            dayComp,
            binding.editTextDescriptionEvent.text.toString(),
            colors[index]
        )
    }

    private fun initButtons() {
        binding.buttonSave.setOnClickListener {
            val eventModel = dayCompiler()
            val i = intent
            i.putExtra("Key", eventModel)
            if (item != null){
                i.putExtra(Constance.EDIT_POSITION,
                    intent.getIntExtra(Constance.EDIT_POSITION, 0)
                )
            }
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