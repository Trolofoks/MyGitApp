package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
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
    var currentDataText: String = dateFormat.format(currentDate)
    lateinit var adapter: EventAdapter
    private var eventsArrayList = arrayListOf<EventModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private var delMode = false
    private var favMode = false
    var currentEventModel: EventModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        eventSharedPrefOutput()
        initLaunch()
        init()
        if (currentEventModel != null){
            selectMainEvent(currentEventModel!!, true)
        }
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

        adapter = EventAdapter(this, currentDataText)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.favorite ->{
                    favoriteMode()
                }
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
        var json = sharedPreferences.getString(Constance.MODEL_LIST, null)
        var type: Type = object : TypeToken<ArrayList<EventModel?>?>() {}.type
        if (json != null){
            eventsArrayList = gson.fromJson<Any>(json, type) as ArrayList<EventModel>
        }
        json = sharedPreferences.getString(Constance.CURRENT_MODEL, null)
        type = object : TypeToken<EventModel?>() {}.type
        if (json != null){
            currentEventModel = gson.fromJson<Any>(json, type) as EventModel?
        }


    }

    private fun eventSharedPrefInput(){
        val editor = sharedPreferences.edit()
        val gson = Gson()
        var json: String = gson.toJson(eventsArrayList)
        editor.putString(Constance.MODEL_LIST, json)
        json = gson.toJson(currentEventModel)
        editor.putString(Constance.CURRENT_MODEL, json)
        editor.apply()
    }

    private fun deletingMode(){
        delMode = !delMode
        if (favMode){
            favoriteMode()
        }
        if (delMode){
            binding.recyclerView.setBackgroundColor(getColor(R.color.red))
        } else {
            binding.recyclerView.setBackgroundColor(getColor(R.color.clear))
        }
    }

    private fun favoriteMode(){
        favMode = !favMode
        if (delMode){
            deletingMode()
        }
        if (favMode){
            binding.recyclerView.setBackgroundColor(getColor(R.color.yellow))
        } else {
            binding.recyclerView.setBackgroundColor(getColor(R.color.clear))
        }

    }

    private fun selectMainEvent(item: EventModel, enterApp : Boolean){
        binding.apply {
            cardViewMainEvent.visibility = View.VISIBLE
            val days = (dateToDays(item.dataOfEvent) - dateToDays(currentDataText)).toString()
            val days2 = minusDesolator(days.toInt())
            val days3 = days2.toString() + " " + afterOrUntil(days.toInt())
            val name = if (item.eventType.length > 22){
                item.eventType.substring(0, 22) + "..."
            } else {
                item.eventType
            }
            textDaysPlusUntilOrAfter.text = days3
            textDescriptionMain.text = item.eventDescription
            textCurrentDataMain.text = item.dataOfEvent
            textMainName.text = name
            favoriteMode()
            if (!enterApp && currentEventModel != null){
                eventsArrayList.add(currentEventModel!!)
            }
            currentEventModel = item
            adapter.addArrayListOfEvent(eventsArrayList)
        }
    }



    override fun onClick(position: Int) {
        if (delMode){
            eventsArrayList.remove(eventsArrayList[position])
            adapter.addArrayListOfEvent(eventsArrayList)

        } else if (favMode){
            val timedEventModel: EventModel = eventsArrayList[position]
            eventsArrayList.remove(eventsArrayList[position])
            selectMainEvent(timedEventModel, false)

        } else {
            Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(Constance.EDIT_INFO, eventsArrayList[position])
            intent.putExtra(Constance.EDIT_POSITION, position)
            launcherEdit?.launch(intent)
        }

    }
}