package com.example.weatherpulse.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpulse.R
import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.databinding.ItemHourBinding

class WeatherListAdapter (private var action: OnHomeClickListener) :
    ListAdapter<WeatherDTO, WeatherListAdapter.ViewHolder>(ProductDiffUtil()) {

    class ProductDiffUtil : DiffUtil.ItemCallback<WeatherDTO>() {
        override fun areItemsTheSame(oldItem: WeatherDTO, newItem: WeatherDTO): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: WeatherDTO, newItem: WeatherDTO): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val itemHourBinding: ItemHourBinding) : RecyclerView.ViewHolder(itemHourBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemHourBinding : ItemHourBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hour, parent, false)
        return ViewHolder(itemHourBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHour = getItem(position)

        holder.itemHourBinding.apply {
            weatherObj = currentHour
            layoutAction = action
        }
    }


}