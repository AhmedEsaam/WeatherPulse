package com.example.weatherpulse.features.locations_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpulse.data.Result
import com.example.weatherpulse.data.source.WeatherRepository
import com.example.weatherpulse.data.source.local.WeatherDatabase
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.RetrofitHelper
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import com.example.weatherpulse.data.source.remote.WeatherService
import com.example.weatherpulse.databinding.FragmentLocationslistBinding
import com.example.weatherpulse.databinding.FragmentSlideshowBinding
import com.example.weatherpulse.features.home.view.WeatherListAdapter
import com.example.weatherpulse.features.home.viewmodel.HomeViewModel
import com.example.weatherpulse.features.home.viewmodel.HomeViewModelFactory
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModel
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModelFactory
import com.example.weatherpulse.features.locations_list.viewmodel.LocationsListViewModel
import com.example.weatherpulse.features.locations_list.viewmodel.LocationsListViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LocationsListFragment : Fragment(), OnLocationsListClickListener {

    private lateinit var binding: FragmentLocationslistBinding

    private lateinit var locationsListViewModelFactory: LocationsListViewModelFactory
    private lateinit var viewModel: LocationsListViewModel

    private lateinit var locationsListAdapter: LocationsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val locationsListViewModel =
            ViewModelProvider(this).get(LocationsListViewModel::class.java)

        binding = FragmentLocationslistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setUpRecyclerView()

        // Getting ViewModel Ready
        locationsListViewModelFactory = LocationsListViewModelFactory(
            WeatherRepository.getRepository(
                WeatherRemoteDataSource.getInstance(RetrofitHelper.instance.create(WeatherService::class.java)),
                WeatherLocalDataSource.getInstance(
                    WeatherDatabase.getInstance(requireActivity()).getWeatherDao()
                )
            )
        )

        viewModel = ViewModelProvider(requireActivity(), locationsListViewModelFactory).get(
            LocationsListViewModel::class.java
        )
//        viewModel.updateLocations()

//        lifecycleScope.launch {
//            viewModel.forecastResult.collectLatest { result ->
//                when (result) {
//                    is Result.Loading -> {
//                    }
//
//                    is Result.Success -> {
//                        locationsListAdapter.submitList(result.data.take(8))
//                        locationsListAdapter.notifyDataSetChanged()
//                    }
//
//                    is Result.Failure -> {
////                        binding.ivLoading.visibility = View.GONE
//                        Snackbar.make(view, "locations list could not load", Snackbar.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }
//        }
    }


    private fun initUI() {

    }


    private fun setUpRecyclerView() {
        locationsListAdapter = LocationsListAdapter(this)

        binding.recyclerLocations.apply {
            adapter = locationsListAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
    }
}