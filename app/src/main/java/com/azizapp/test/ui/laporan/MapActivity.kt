package com.azizapp.test.ui.laporan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentNamaJalanBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_daftar.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.map
import kotlinx.android.synthetic.main.fragment_nama_jalan.*
import java.lang.StringBuilder
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    var city = ""
    var address = ""
    var lat: Double = 0.0
    var long: Double = 0.0

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var binding: FragmentNamaJalanBinding
    private val mapActivityViewModel: MapActivityViewModel by viewModels()
    var marker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nama_jalan)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_nama_jalan)
        binding.apply {
            lifecycleOwner = this@MapActivity
            viewModelJalan = mapActivityViewModel
        }
        map.onCreate(savedInstanceState)
        map.onResume()

        map.getMapAsync(this)

        mapActivityViewModel.action.observe(this, Observer { action ->
            when (action) {
                MapActivityViewModel.ACTION_SUCCESS -> actionSuccess()
                MapActivityViewModel.ACTION_FAILED -> actionFailed()
            }
        })

    }

    private fun actionFailed() {
        Toast.makeText(this, "Gagal", Toast.LENGTH_LONG).show()
    }

    private fun actionSuccess() {
        btn_simpan.setOnClickListener {
            val output = Intent()
            output.putExtra("ADDRESS", address)
            output.putExtra("CITY", city)
            output.putExtra("LAT", lat)
            output.putExtra("LONG", long)
            setResult(RESULT_OK, output)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val pekanbaru = LatLng(0.510440, 101.438309)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pekanbaru))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f))
        googleMap.setOnMapClickListener { point ->
            mapActivityViewModel.changeStreetName(this, point.latitude, point.latitude)
            address = mapActivityViewModel.namaJalan.value.toString()
            lat = mapActivityViewModel.latitude.value?.toDouble()!!
            long = mapActivityViewModel.longitude.value?.toDouble()!!
            Toast.makeText(
                this,
                mapActivityViewModel.namaJalan.toString(),
                Toast.LENGTH_SHORT
            ).show()
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

        editTextNamaJalan.setText(address)
        editTextLokasi.setText(StringBuilder("[${lat},${long}]"))
    }
}