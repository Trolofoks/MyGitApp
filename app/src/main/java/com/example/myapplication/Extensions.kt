package com.example.myapplication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class Extensions : AppCompatActivity() {
    fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializableExtra(key, m_class)!!
        else
            this.getSerializableExtra(key) as T
    }
}