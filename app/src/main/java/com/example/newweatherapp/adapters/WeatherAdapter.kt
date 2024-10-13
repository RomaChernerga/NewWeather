package com.example.newweatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newweatherapp.R
import com.example.newweatherapp.databinding.ItemViewBinding
import com.squareup.picasso.Picasso

class WeatherAdapter(val listener : Listener?) : ListAdapter<WeatherModel, WeatherAdapter.MyViewHolder>(Camporator()) {

    class MyViewHolder(view: View, private val listener: Listener?) : ViewHolder(view) {
        private val binding = ItemViewBinding.bind(view)
        var itemTemp: WeatherModel? = null
        init {
            itemView.setOnClickListener {
                itemTemp?.let { it1 -> listener?.onClick(it1) }
            }
        }
        fun bind(item: WeatherModel) = with(binding) {
            itemTemp = item
            tViewDate.text = item.time
            tViewCondition.text = item.condition
            tViewTemp.text = item.currentTemp.ifEmpty { "${item.maxTemp}'C / ${item.minTemp}" }
            Picasso.get().load("https:" + item.imageUrl).into(imView)
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
        return MyViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listener {
        fun onClick(item : WeatherModel)
    }



}


