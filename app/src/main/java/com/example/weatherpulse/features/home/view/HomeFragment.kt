package com.example.weatherpulse.features.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpulse.R
import com.example.weatherpulse.data.Result
import com.example.weatherpulse.data.source.WeatherRepository
import com.example.weatherpulse.data.source.local.WeatherDatabase
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.RetrofitHelper
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import com.example.weatherpulse.data.source.remote.WeatherService
import com.example.weatherpulse.databinding.FragmentHomeBinding
import com.example.weatherpulse.features.home.viewmodel.HomeViewModel
import com.example.weatherpulse.features.home.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.create

class HomeFragment : Fragment(), OnHomeClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    private lateinit var weatherListAdapter: WeatherListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting ViewModel Ready
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepository.getRepository(
                WeatherRemoteDataSource.getInstance(RetrofitHelper.instance.create(WeatherService::class.java)),
                WeatherLocalDataSource.getInstance(WeatherDatabase.getInstance(requireActivity()).getWeatherDao()
                )
            )
        )

        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        initUI()

        setUpRecyclerView()


        lifecycleScope.launch {

            // Get Current Weather Data ...................................
            viewModel.getCurrentWeather()
            viewModel.currentWeatherResult.collectLatest { result ->
                when (result) {
                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        binding.weatherObj = result.data
                    }
                    is Result.Failure -> {
                    }
                }
            }

            // Get Weather forecast Data ...................................
            viewModel.getWeatherForecast()
            viewModel.forecastResult.collectLatest { result ->
                when (result) {
                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        weatherListAdapter.submitList(result.data)
                        weatherListAdapter.notifyDataSetChanged()
                    }
                    is Result.Failure -> {
                    }
                }
            }
        }
    }


    private fun initUI() {
        binding.lifecycleOwner = this
    }

    private fun setUpRecyclerView() {
        weatherListAdapter = WeatherListAdapter(this)

        binding.recyclerHours.apply {
            adapter = weatherListAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }
    }
}