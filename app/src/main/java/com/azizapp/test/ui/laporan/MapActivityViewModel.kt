package com.azizapp.test.ui.laporan

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class MapActivityViewModel : ViewModel() {

    var address = ""
    var city = ""
    var lat: Double = 0.0
    var long: Double = 0.0

    lateinit var geocoder: Geocoder
    lateinit var addresses: MutableList<Address>

    companion object {
        const val ACTION_SUCCESS = "ACTION_SUCCESS"
        const val ACTION_FAILED = "ACTION_FAILED"
    }

    val namaJalan = MutableLiveData<String>()
    val action = MutableLiveData<String>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()

    fun changeStreetName(context: Context, lat: Double, long: Double) {
        geocoder = Geocoder(context, Locale.getDefault())
        addresses = geocoder.getFromLocation(lat, long, 1)
        address = addresses[0].getAddressLine(0).toString()
        city = addresses[0].locality.toString()
        latitude.postValue(lat)
        longitude.postValue(long)
        namaJalan.postValue("$address $city")


    }

    fun btnConfirmClick() {
        viewModelScope.launch {
            if (!namaJalan.value.isNullOrEmpty()) {
                action.postValue(ACTION_SUCCESS)
            } else {
                action.postValue(ACTION_FAILED)
            }
        }
    }
}