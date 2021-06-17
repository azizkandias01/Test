package com.azizapp.test.ui.laporan

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azizapp.test.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_nama_jalan.*
import java.lang.reflect.InvocationTargetException
import java.util.*


class LaporanActivity : AppCompatActivity(), OnMapReadyCallback {

    var address = ""
    var city = ""
    var lat: Double = 0.0
    var long: Double = 0.0

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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val pekanbaru = LatLng(0.510440, 101.438309)
        val geocoder = Geocoder(this, Locale.getDefault())


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pekanbaru))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        googleMap.setOnMapClickListener { point ->
            try {
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
}