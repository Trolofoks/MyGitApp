package com.example.myapplication

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

class MainActivity : AppCompatActivity(), EventAdapter.Listener {

    private var launcherAdd: ActivityResultLauncher<Intent>? = null
    private var launcherEdit: ActivityResultLauncher<Intent>? = null
    lateinit var binding: ActivityMainBinding
    //получить дату
    var currentDate: Date = Date()
    //шаблон для форматирования даты
    var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    //дата отфармотированная
    var dateText: String = dateFormat.format(currentDate)
    lateinit var adapter: EventAdapter
    private var eventsArrayList = arrayListOf<EventModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private var delMode = false

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
        launcherAdd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
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
        launcherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("Key", EventModel::class.java)!!
                } else {
                    it.data?.getSerializableExtra("Key")!! as EventModel
                }
                val position = it.data?.getIntExtra(Constance.EDIT_POSITION, 0)
                eventsArrayList[position!!] = item
                adapter.addArrayListOfEvent(eventsArrayList)
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
                    deletingMode()
                }
                R.id.add -> {
                    launcherAdd?.launch(Intent(this,AddActivity::class.java))
                }
            }
            true
        }

        adapter.addArrayListOfEvent(eventsArrayList)
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

    private fun deletingMode(){
        delMode = !delMode
        if (delMode){
            binding.recyclerView.setBackgroundColor(getColor(R.color.red))
        } else {
            binding.recyclerView.setBackgroundColor(getColor(R.color.clear))
        }
    }



    override fun onClick(position: Int) {
        if (!delMode){
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(Constance.EDIT_INFO, eventsArrayList[position])
            intent.putExtra(Constance.EDIT_POSITION, position)
            launcherEdit?.launch(intent)
        } else {
            eventsArrayList.remove(eventsArrayList[position])
            adapter.addArrayListOfEvent(eventsArrayList)
        }

    }
}