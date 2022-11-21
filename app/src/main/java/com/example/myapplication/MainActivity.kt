package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

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
    var eventsArrayList = arrayListOf<EventModel>()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        eventSharedPrefOutput()
        initLaunch()
        init()

    }

    override fun onStop() {
        super.onStop()
        eventSharedPrefInput()
    }

    private fun initLaunch(){
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                //чтобы на старых версиях работало
                val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("Key", EventModel::class.java)!!
                } else {
                    it.data?.getSerializableExtra("Key")!! as EventModel
                }

                eventsArrayList.add(item)
                Log.d("MyLog", "${eventsArrayList}")
                adapter.addEvent(item)
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
        for (i in eventsArrayList){
            adapter.addEvent(i)
        }
    }

    private fun eventSharedPrefOutput() {
        val gson = Gson()
        val json = sharedPreferences.getString("Key", null)
        val type: Type = object : TypeToken<ArrayList<EventModel?>?>() {}.type
        if (json != null){
            eventsArrayList = gson.fromJson<Any>(json, type) as ArrayList<EventModel>
        }



    }

    private fun eventSharedPrefInput(){
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(eventsArrayList)
        editor.putString("Key", json)
        editor.apply()
    }



    override fun onClick(position: Int) {
        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
    }
}