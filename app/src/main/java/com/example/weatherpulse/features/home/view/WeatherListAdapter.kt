package com.example.weatherpulse.features.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherpulse.R
import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.databinding.ItemHourBinding

class WeatherListAdapter(private var action: OnHomeClickListener) :
    ListAdapter<WeatherDTO, WeatherListAdapter.ViewHolder>(ProductDiffUtil()) {

    private lateinit var binding: ItemHourBinding

    class ProductDiffUtil : DiffUtil.ItemCallback<WeatherDTO>() {
        override fun areItemsTheSame(oldItem: WeatherDTO, newItem: WeatherDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherDTO, newItem: WeatherDTO): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: ItemHourBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemHourBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHour = getItem(position)

        holder.binding.apply {
            tvHour.text = currentHour.dtDateComponents.hour12Format
            tvHourTemp.text = currentHour.main?.tempInt.toString() + "°"
            Glide.with(holder.itemView.context)
                .load("https://openweathermap.org/img/wn/" + currentHour.weather?.get(0)?.icon + "@2x.png")
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                )
                .into(ivHourImg)

        }
    }


}