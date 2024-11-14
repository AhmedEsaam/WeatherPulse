package com.example.weatherpulse.features.home.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherpulse.R
import com.example.weatherpulse.data.Result
import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.data.source.WeatherRepository
import com.example.weatherpulse.data.source.local.WeatherDatabase
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.RetrofitHelper
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import com.example.weatherpulse.data.source.remote.WeatherService
import com.example.weatherpulse.databinding.FragmentHomeBinding
import com.example.weatherpulse.features.home.viewmodel.HomeViewModel
import com.example.weatherpulse.features.home.viewmodel.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.net.Network
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class HomeFragment : Fragment(), OnHomeClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    private lateinit var weatherListAdapter: WeatherListAdapter

    // Network
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallback
    private lateinit var networkStatusIndicator: TextView
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUI()
        setUpRecyclerView()

        // Getting ViewModel Ready
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepository.getRepository(
                WeatherRemoteDataSource.getInstance(RetrofitHelper.instance.create(WeatherService::class.java)),
                WeatherLocalDataSource.getInstance(WeatherDatabase.getInstance(requireActivity()).getWeatherDao())
            )
        )

        viewModel = ViewModelProvider(requireActivity(), homeViewModelFactory).get(HomeViewModel::class.java)
        viewModel.updateWeatherData()

        checkNetworkConnectivity()


        // Get Current Weather Data ...................................
        lifecycleScope.launch {
            viewModel.currentWeatherResult.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        Glide.with(this@HomeFragment)
                            .asGif()
                            .load(R.drawable.loading)
                            .into(binding.ivCurrentImg)
                    }

                    is Result.Success -> {
                        val currentWeather: WeatherDTO = result.data
                        if (currentWeather != null) {
                            binding.apply{
                                tvName.text = currentWeather.name ?: "N/A"
                                tvDescription.text = currentWeather.weather?.get(0)?.description ?: "N/A"
                                tvMainTemp.text = getString(R.string.tempValue, currentWeather.main?.tempInt ?: 0)
                                tvfeels.text = getString(R.string.tempValue, currentWeather.main?.feelsLikeInt ?: 0)
                                tvHumidity.text = getString(R.string.percentage, currentWeather.main?.humidity ?: 0)
                                tvPressure.text = getString(R.string.pressureValue, currentWeather.main?.pressure ?: 0)
                                tvWindSpeed.text = getString(R.string.windSpeedValue, currentWeather.wind?.speed ?: 0.00)
                                tvSunrise.text = currentWeather.sys?.sunriseDateComponents?.hour.toString() + ":"  + currentWeather.sys?.sunriseDateComponents?.minute.toString()
                                tvSunset.text = currentWeather.sys?.sunsetDateComponents?.hour.toString() + ":"  + currentWeather.sys?.sunsetDateComponents?.minute.toString()

                                Glide.with(this@HomeFragment)
                                    .load("https://openweathermap.org/img/wn/" + currentWeather.weather?.get(0)?.icon + "@2x.png")
                                    .apply(
                                        RequestOptions()
                                            .override(200, 200)
                                            .placeholder(R.drawable.ic_launcher_foreground)
                                            .error(R.drawable.ic_launcher_foreground)
                                    )
                                    .into(ivCurrentImg)
                            }
                        }
                    }

                    is Result.Failure -> {
                        result.exception.localizedMessage?.let { Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show() }
                    }
                }
            }
        }

        // Get Weather forecast Data ...................................
        lifecycleScope.launch {
            viewModel.forecastResult.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
//                        binding.ivLoading.visibility = View.VISIBLE
//                        binding.recyclerHours.visibility = View.GONE
                    }

                    is Result.Success -> {
//                        binding.ivLoading.visibility = View.GONE
//                        binding.recyclerHours.visibility = View.VISIBLE
                        weatherListAdapter.submitList(result.data.take(8))
                        weatherListAdapter.notifyDataSetChanged()
                    }

                    is Result.Failure -> {
//                        binding.ivLoading.visibility = View.GONE
                        result.exception.localizedMessage?.let { Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show() }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        handler.removeCallbacksAndMessages(null)

    }

    private fun initUI() {

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



    private var isFirstLaunch = true // Flag to track first launch

    private fun checkNetworkConnectivity() {

        networkStatusIndicator = binding.tvNetwork

        // Setting up ConnectivityManager
        connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Register network callback to monitor connectivity
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                handler.post {
                    if (!isFirstLaunch) {
                        showNetworkIndicator(getString(R.string.network_success), android.R.color.holo_green_light)
                    }
                    viewModel.updateWeatherData()
                }
            }

            override fun onLost(network: Network) {
                handler.post {
                    showNetworkIndicator(getString(R.string.network_fail), R.color.redV)
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Check initial network status
        val isConnected = connectivityManager.activeNetworkInfo?.isConnected == true
        if (!isConnected) {
            showNetworkIndicator(getString(R.string.network_fail), R.color.redV)
        }
        isFirstLaunch = false // Set to false after first network check
    }

    private fun showNetworkIndicator(message: String, colorRes: Int) {
        networkStatusIndicator.text = message
        networkStatusIndicator.setBackgroundColor(resources.getColor(colorRes))
        networkStatusIndicator.visibility = View.VISIBLE

        // Hide after 2 seconds only if network is on
        if (colorRes == android.R.color.holo_green_light) {
            handler.postDelayed({ networkStatusIndicator.visibility = View.GONE }, 2000)
        }
    }



    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnectedOrConnecting == true
    }

}