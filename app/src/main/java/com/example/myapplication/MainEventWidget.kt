package com.example.myapplication

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

//получить дату
var currentDate: Date = Date()
//шаблон для форматирования даты
var dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
//дата отфармотированная
var currentDataText: String = dateFormat.format(currentDate)
/**
 * Implementation of App Widget functionality.
 */
class MainEventWidget : AppWidgetProvider() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currentEventModel: EventModel
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.main_event_widget)
    //views.setTextViewText(R.id.appwidget_text, widgetText)
    lateinit var currentEventModel: EventModel
    val sharedPreferences = context.getSharedPreferences(
        "shared preferences",
        AppCompatActivity.MODE_PRIVATE
    )
    val gson = Gson()
    val json = sharedPreferences.getString(Constance.CURRENT_MODEL, null)
    val type = object : TypeToken<EventModel?>() {}.type
    if (json != null) {
        currentEventModel = (gson.fromJson<Any>(json, type) as EventModel?)!!
    }
    val days = (dateToDays(currentEventModel.dataOfEvent) - dateToDays(currentDataText)).toString()
    val days2 = minusDesolator(days.toInt())

    views.setTextViewText(R.id.textDaysWidget, days2.toString())
    views.setTextViewText(R.id.textNameEventWidget, currentEventModel.eventType)
    views.setTextViewText(R.id.textAfterOrUntil, afterOrUntil(days.toInt()))
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)


}

fun minusDesolator(Date: Int): Int{
    return if (Date < 0) {
        0 - Date
    } else {
        Date
    }
}
fun dateToDays(data: String): Int {
    val dataParts = data.split(".").toTypedArray()
    return (dataParts[2].toInt() * 365) + (dataParts[1].toInt() * 30) + (dataParts[0].toInt())
}
fun afterOrUntil(Date: Int): String{
    return if (Date > 0){
        "Days_Until"
    } else {
        "Days_After"
    }
}