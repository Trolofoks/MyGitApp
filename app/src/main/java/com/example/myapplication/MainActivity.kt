package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), EventAdapter.Listener {

    private var launcher: ActivityResultLauncher<Intent>? = null
    lateinit var binding: ActivityMainBinding
    //получить дату
    var currentDate: Date = Date()
    //шаблон для форматирования даты
    var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    //дата отфармотированная
    var dateText: String = dateFormat.format(currentDate)
    lateinit var adapter: EventAdapter
    private var EventsArrayList : ArrayList<EventModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLaunch()
        init()

    }
    private fun initLaunch(){
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("Key", EventModel::class.java)!!
                } else {
                    it.data?.getSerializableExtra("Key")!! as EventModel
                }
                Log.d("MyLog", "$item")
                EventsArrayList?.add(item)
                adapter.submitList(EventsArrayList)
            }
        }
    }
    private fun init(){
        adapter = EventAdapter(this, dateText)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.delete -> {
                    Toast.makeText(this, "$dateText", Toast.LENGTH_SHORT).show()
                }
                R.id.add -> {
                    launcher?.launch(Intent(this,AddActivity::class.java))
                }
            }
            true
        }


    }

    override fun onClick(position: Int) {
        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
    }
}