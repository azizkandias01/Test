package com.azizapp.test.ui.laporan

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentNamaJalanBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.map
import kotlinx.android.synthetic.main.fragment_nama_jalan.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    var city = ""
    var address = ""
    var lat: Double = 0.0
    var long: Double = 0.0

    lateinit var binding: FragmentNamaJalanBinding
    private val mapActivityViewModel: MapActivityViewModel by viewModels()


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
            googleMap.clear()
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(point.latitude, point.longitude))
            )
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
}