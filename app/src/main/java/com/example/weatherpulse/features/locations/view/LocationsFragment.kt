package com.example.weatherpulse.features.locations.view

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherpulse.R
import com.example.weatherpulse.data.source.WeatherRepository
import com.example.weatherpulse.data.source.local.WeatherDatabase
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.RetrofitHelper
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import com.example.weatherpulse.data.source.remote.WeatherService
import com.example.weatherpulse.databinding.FragmentLocationsBinding
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModel
import com.example.weatherpulse.features.locations.viewmodel.LocationsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

class LocationsFragment : Fragment(), OnLocationsClickListener {

    private lateinit var binding: FragmentLocationsBinding

    private lateinit var locationsViewModelFactory: LocationsViewModelFactory
    private lateinit var viewModel: LocationsViewModel

    private lateinit var locationsListAdapter: LocationsListAdapter

    private lateinit var mapView: MapView
    private var marker: org.osmdroid.views.overlay.Marker? = null
    private var lng: Double = 31.2355
    private var lat: Double = 30.0441

    private var isLocationSet = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // inflate layout
        binding = FragmentLocationsBinding.inflate(inflater, container, false)

        Configuration.getInstance().userAgentValue = getString(R.string.app_name)

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
                WeatherLocalDataSource.getInstance(
                    WeatherDatabase.getInstance(requireActivity()).getWeatherDao()
                )
            )
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            locationsViewModelFactory
        ).get(LocationsViewModel::class.java)

        updateMapLocation()

        viewModel.location.observe(viewLifecycleOwner) { location ->
            lng = location.first
            lat = location.second

            if (!isLocationSet) {
                updateMapLocation()
                isLocationSet = true  // Set flag to true so that we don't update the map again
            }
        }



        // Set listener for map tap events
        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    Snackbar.make(
                        requireView(),
                        "Lat: $latitude, Lon: $longitude",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    addMarkerAtLocation(it)
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

        })

        mapView.overlays.add(mapEventsOverlay)

    }


    private fun initUI() {
        mapView = binding.mapView
        marker = org.osmdroid.views.overlay.Marker(mapView)
    }


    private fun setUpRecyclerView() {

    }

    private fun updateMapLocation() {
        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            // Center map to a default location (optional)
            val mapController = controller
            mapController.setZoom(10.0)
            val startPoint = GeoPoint(lat, lng) // Example coordinates
            mapController.setCenter(startPoint)
        }
    }

    // Function to add a marker at the given location
    private fun addMarkerAtLocation(location: GeoPoint) {
        marker?.let {
            mapView.overlays.remove(it)
        }

        marker?.apply {
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_location_on_50)
            position = location
            title = "Location"
            snippet = "Lat: ${location.latitude}, Lon: ${location.longitude}"
            mapView.overlays.add(this)
            mapView.invalidate()
        }
    }

//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        permissions.entries.forEach { permission ->
//            val isGranted = permission.value
//            if (!isGranted) {
//                // Permission was denied, handle appropriately
//            }
//        }
//    }
//
//    private fun requestPermissions() {
//        requestPermissionLauncher.launch(arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ))
//    }

}