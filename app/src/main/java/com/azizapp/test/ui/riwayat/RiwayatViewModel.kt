package com.azizapp.test.ui.riwayat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizapp.test.model.Pengaduan
import com.azizapp.test.repository.MainRepository
import com.azizapp.test.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RiwayatViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val loadingEnable = MutableLiveData<Boolean>()
    val listPengaduan: ArrayList<Pengaduan> = arrayListOf()
    val action = MutableLiveData<String>()
    val pengaduan = MutableLiveData<ArrayList<Pengaduan>>()

    companion object {
        const val ACTION_RIWAYAT_FETCHED = "RIWAYAT_FETCHED"
        const val ACTION_RIWAYAT_ERROR = "RIWAYAT_ERROR"
    }

    fun onLoad() {
        loadingEnable.value = true
        val bearer: String = "Bearer " + com.azizapp.test.utill.Session.bearer
        viewModelScope.launch {
            when (val response = bearer.let { repository.getPengaduanMasyarakat(it) }) {
                is Resource.Success -> {
                    response.data?.forEach {
                        listPengaduan.add(it)
                    }
                    pengaduan.postValue(listPengaduan)
                    action.postValue(ACTION_RIWAYAT_FETCHED)
                    loadingEnable.postValue(false)
                }
                is Resource.Error -> {
                    loadingEnable.postValue(false)
                    action.postValue(ACTION_RIWAYAT_ERROR)
                    loadingEnable.postValue(false)
                }
            }
        }
    }

}