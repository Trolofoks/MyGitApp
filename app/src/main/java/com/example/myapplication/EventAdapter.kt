package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemBinding
import java.util.Date

class EventAdapter(val currentData: String) : ListAdapter<EventModel, EventAdapter.Holder>(Comparator()) {

    class Holder(view: View, val currentData: String) : RecyclerView.ViewHolder(view){

        val binding = ListItemBinding.bind(view)
        var itemEvent: EventModel? = null

        fun bind(item: EventModel) = with(binding) {
            val howMuchDays = dateToDays(item.dataOfEvent) - dateToDays(currentData)
            textCurrentData.text = item.dataOfEvent
            textName.text = item.eventType
            textAfterOrUntil.text = afterOrUntil(howMuchDays)
            textDays.text = minusDesolator(howMuchDays).toString()
        }
        fun minusDesolator(Date: Int): Int{
            return if (Date > 0) {
                0 - Date
            } else {
                Date
            }
        }
        fun afterOrUntil(Date: Int): String{
            return if (Date > 0){
                "Days_Until"
            } else {
                "Days_After"
            }

        }
        fun dateToDays(data: String): Int {
            val day = currentData.substring(0, 1).toInt()
            val month = currentData.substring(3, 4).toInt()
            val year = currentData.substring(6, 7).toInt()
            return year * 365 + month * 30 + day
        }
    }


    class Comparator: DiffUtil.ItemCallback<EventModel>(){
        override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel): Boolean {
            return oldItem == newItem
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view, currentData)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}