package com.example.myapplication

import android.appwidget.AppWidgetProvider
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

fun AppCompatActivity.dateToDays(data: String): Int {
    //ОБОЖЕ Я ЛЮБЛЮ split
    val dataParts = data.split(".").toTypedArray()
    return (dataParts[2].toInt() * 365) + (dataParts[1].toInt() * 30) + (dataParts[0].toInt())
}

fun AppCompatActivity.afterOrUntil(Date: Int): String{
    return if (Date > 0){
        "Days_Until"
    } else {
        "Days_After"
    }
}
fun AppCompatActivity.minusDesolator(Date: Int): Int{
    return if (Date < 0) {
        0 - Date
    } else {
        Date
    }
}




