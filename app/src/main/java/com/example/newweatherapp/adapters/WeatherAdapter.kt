package com.example.newweatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newweatherapp.R
import com.example.newweatherapp.databinding.ItemViewBinding

class WeatherAdapter : ListAdapter<WeatherModel, WeatherAdapter.MyViewHolder>(Camporator()) {

    class MyViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemViewBinding.bind(view)
        fun bind(item: WeatherModel) = with(binding) {
            tViewDate.text = item.time
            tViewCondition.text = item.condition
            tVewTemp.text = item.currentTemp
        }

    }

    class Camporator : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


