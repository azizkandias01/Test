package com.azizapp.test.ui.laporan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.azizapp.test.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_nama_jalan.*
import java.lang.reflect.InvocationTargetException
import java.util.*


class LaporanActivity : AppCompatActivity(), OnMapReadyCallback {

    var address = ""
    var city = ""
    var lat: Double = 0.0
    var long: Double = 0.0

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    var locationPermissionGranted: Boolean? = null

    private val defaultLocation = LatLng(0.510440, 101.438309)
    private var lastKnownLocation: Location? = null

    companion object {
        private val TAG = LaporanActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nama_jalan)

        map.onCreate(savedInstanceState)
        map.onResume()

        map.getMapAsync(this)

        btn_simpan.setOnClickListener {
            val output = Intent()
            output.putExtra("ADDRESS", address)
            output.putExtra("CITY", city)
            output.putExtra("LAT", lat)
            output.putExtra("LONG", long)
            setResult(RESULT_OK, output)
            finish()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.mMap = googleMap
        val geocoder = Geocoder(this, Locale.getDefault())

        this.mMap?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

//            override fun getInfoContents(p0: Marker?): View {
//                TODO("Not yet implemented")
//            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(
                    R.layout.custom_info_contents,
                    findViewById<FrameLayout>(R.id.map), false
                )
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission()
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()
        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        googleMap.setOnMapClickListener { point ->
            try {
                googleMap.clear()
                var latLng = LatLng(point.latitude, point.longitude)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                )
                val addresses: MutableList<Address>? =
                    geocoder.getFromLocation(point.latitude, point.longitude, 1)
                address = addresses?.get(0)?.getAddressLine(0).toString()
                city = addresses?.get(0)?.locality.toString()
                lat = point.latitude
                long = point.longitude
                Toast.makeText(
                    this,
                    "$address, $city",
                    Toast.LENGTH_SHORT
                ).show()
                tv_namaJalan.setText("$address, $city")
            } catch (e: InvocationTargetException) {
                Toast.makeText(
                    this,
                    "Kesalahan Terjadi Saat Pengambilan Data! : ${e.cause.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted!!) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted!!) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }
}