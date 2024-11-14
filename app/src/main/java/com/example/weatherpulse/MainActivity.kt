package com.example.weatherpulse

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherpulse.data.source.WeatherRepository
import com.example.weatherpulse.data.source.local.WeatherDatabase
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.RetrofitHelper
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import com.example.weatherpulse.data.source.remote.WeatherService
import com.example.weatherpulse.databinding.ActivityMainBinding
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModel
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import java.io.IOException

private const val MY_LOCATION_PERMISSION_ID = 1001

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var locationsViewModel: LocationsViewModel
    private lateinit var locationsViewModelFactory: LocationsViewModelFactory

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentAddress: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_locations,
                R.id.nav_locationslist,
                R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Getting ViewModel Ready
        locationsViewModelFactory = LocationsViewModelFactory(
            WeatherRepository.getRepository(
                WeatherRemoteDataSource.getInstance(RetrofitHelper.instance.create(WeatherService::class.java)),
                WeatherLocalDataSource.getInstance(WeatherDatabase.getInstance(this).getWeatherDao()
                )
            )
        )
        // Initialize ViewModel
        locationsViewModel = ViewModelProvider(this, locationsViewModelFactory).get(LocationsViewModel::class.java)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation()
            } else {
                enableLocationServices()
            }

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_LOCATION_PERMISSION_ID
            )
        }
    }

    fun checkPermissions(): Boolean {
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        // Get Fused Location Provider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),

            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    Log.i("TAG", "onLocationResult: ==== ${location.locations.toString()}")
                    val lng: Double? = location.lastLocation?.longitude
                    val lat: Double? = location.lastLocation?.latitude

                    currentAddress = getAddress(lng ?: 0.0, lat ?: 0.0)

                    // Pass longitude and latitude to ViewModel
                    locationsViewModel.setLocation(lng ?: 0.0, lat ?: 0.0)

                }
            },

            Looper.myLooper()
        )
    }

    fun enableLocationServices() {
        Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun getAddress(lng: Double, lat: Double): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        var addressText: String = ""

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                addressText = addresses[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

}