package com.example.weatherpulse.features.locations.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherpulse.data.source.WeatherRepository
import com.example.weatherpulse.data.source.local.WeatherDatabase
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.RetrofitHelper
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import com.example.weatherpulse.data.source.remote.WeatherService
import com.example.weatherpulse.databinding.FragmentGalleryBinding
import com.example.weatherpulse.features.home.view.WeatherListAdapter
import com.example.weatherpulse.features.home.viewmodel.HomeViewModel
import com.example.weatherpulse.features.home.viewmodel.HomeViewModelFactory
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModel
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModelFactory

class LocationsFragment : Fragment(), OnLocationsClickListener {

    private lateinit var binding: FragmentGalleryBinding

    private lateinit var locationsViewModelFactory: LocationsViewModelFactory
    private lateinit var viewModel: LocationsViewModel

    private lateinit var locationsListAdapter: LocationsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val locationsViewModel =
            ViewModelProvider(this).get(LocationsViewModel::class.java)

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setUpRecyclerView()

        // Getting ViewModel Ready
        locationsViewModelFactory = LocationsViewModelFactory(
            WeatherRepository.getRepository(
                WeatherRemoteDataSource.getInstance(RetrofitHelper.instance.create(WeatherService::class.java)),
                WeatherLocalDataSource.getInstance(WeatherDatabase.getInstance(requireActivity()).getWeatherDao())
            )
        )

        viewModel = ViewModelProvider(requireActivity(), locationsViewModelFactory).get(LocationsViewModel::class.java)

    }


    private fun initUI() {

    }


    private fun setUpRecyclerView() {

    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}