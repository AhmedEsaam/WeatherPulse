package com.example.weatherpulse.features.locations_list.view

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherpulse.data.LocationDTO
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpulse.databinding.ItemLocationBinding

class LocationsListAdapter(private var action: OnLocationsListClickListener) :
    ListAdapter<LocationDTO, LocationsListAdapter.ViewHolder>(ProductDiffUtil()) {

    private lateinit var binding: ItemLocationBinding

    class ProductDiffUtil : DiffUtil.ItemCallback<LocationDTO>() {
        override fun areItemsTheSame(oldItem: LocationDTO, newItem: LocationDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationDTO, newItem: LocationDTO): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.apply {
            tvLocation.text = current.name

        }
    }

}