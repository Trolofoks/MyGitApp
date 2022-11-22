package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemBinding

class EventAdapter(val listener: Listener, val currentData: String) : ListAdapter<EventModel, EventAdapter.Holder>(Comparator()) {

    val eventModelList = ArrayList<EventModel>()

    class Holder(view: View, val listener: Listener) : RecyclerView.ViewHolder(view){

        val binding = ListItemBinding.bind(view)
        var itemEvent: EventModel? = null

        fun bind(item: EventModel, currentData : String) = with(binding) {
            val howMuchDays = dateToDays(item.dataOfEvent) - dateToDays(currentData)
            textCurrentData.text = item.dataOfEvent
            textName.text = correctName(item.eventType)
            textAfterOrUntil.text = afterOrUntil(howMuchDays)
            textDays.text = minusDesolator(howMuchDays).toString()
            cardBackgroundColor.setCardBackgroundColor(item.Color)

            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }

        private fun correctName(name: String): String{
            if (name.length > 40){
                return name.substring(0, 35) + "..."
            } else
                return name
        }

        fun minusDesolator(Date: Int): Int{
            return if (Date < 0) {
                0 - Date
            } else {
                Date
            }
        }
        private fun afterOrUntil(Date: Int): String{
            return if (Date > 0){
                "Days_Until"
            } else {
                "Days_After"
            }

        }
        private fun dateToDays(data: String): Int {
            //ОБОЖЕ Я ЛЮБЛЮ split
            val dataParts = data.split(".").toTypedArray()
            return (dataParts[2].toInt() * 365) + (dataParts[1].toInt() * 30) + (dataParts[0].toInt())


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
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(eventModelList[position], currentData)
    }

    override fun getItemCount(): Int {
        return eventModelList.size
    }

    fun addEvent(event: EventModel){
        eventModelList.add(event)
        notifyDataSetChanged()
    }

    fun addArrayListOfEvent(eventArrayList: ArrayList<EventModel>){
        eventModelList.clear()
        eventModelList.addAll(eventArrayList)
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(position: Int)
    }
}