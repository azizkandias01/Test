package com.azizapp.test.ui.laporan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.azizapp.test.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_laporan.*
import kotlinx.android.synthetic.main.fragment_nama_jalan.*
import java.lang.reflect.InvocationTargetException
import java.util.*


class LaporanActivity : AppCompatActivity(), OnMapReadyCallback {

    var address = "";
    var city = "";
    var lat: Double = 0.0
    var long: Double = 0.0

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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

        val btnGetCurrentLocation: FloatingActionButton = findViewById(R.id.fab_get_current_location)
        btnGetCurrentLocation.setOnClickListener {
            fetchLocation()
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val pekanbaru = LatLng(0.510440, 101.438309)
        val geocoder = Geocoder(this, Locale.getDefault())


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pekanbaru))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
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

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                getAddress(it.latitude, it.longitude)
                Toast.makeText(
                    this,
                    "${it.latitude}, ${it.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: MutableList<Address>? =
            geocoder.getFromLocation(latitude, longitude, 1)
        address = addresses?.get(0)?.getAddressLine(0).toString()
        city = addresses?.get(0)?.locality.toString()
        lat = latitude
        long = longitude

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
        tv_namaJalan.setText(address)
        Toast.makeText(
            this,
            "$address, $city",
            Toast.LENGTH_SHORT
        ).show()
        //editTextLokasi.setText("[${lat},${long}]")
    }
}