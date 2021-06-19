package com.azizapp.test.ui.laporan

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizapp.test.model.DataPengaduanMasyarakat
import com.azizapp.test.repository.MainRepository
import com.azizapp.test.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaporanViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    companion object {
        const val ACTION_SUCCESS = "ACTION_SUCCESS"
        const val ACTION_FAILED = "ACTION_FAILED"
        const val ACTION_ERROR = "ACTION_ERROR"
    }

    val namaJalan = MutableLiveData<String>()
    val lokasi = MutableLiveData<String>()
    val img = MutableLiveData<Uri>()
    val deskripsi = MutableLiveData<String>()
    val tipe_pengaduan = MutableLiveData<String>()
    val loadingEnable = MutableLiveData<Boolean>()
    val action = MutableLiveData<String>()

    fun uploadLaporan(
        bearer: String,
        namaJalan: String,
        image: String?,
        deskripsi: String,
        tipePengaduan: String,
        geometry: String,
        statusPengaduan: String
    ) {

        loadingEnable.value = true

        viewModelScope.launch {

            if (namaJalan.isEmpty() && deskripsi.isEmpty() && tipePengaduan.isEmpty()) {
                loadingEnable.postValue(false)
                action.postValue(ACTION_FAILED)
            } else {
                val masyarakat = DataPengaduanMasyarakat(
                    namaJalan,
                    image,
                    tipePengaduan,
                    deskripsi,
                    statusPengaduan,
                    geometry
                )
                when (repository.masyarakatLaporan(
                    bearer,
                    masyarakat
                )) {
                    is Resource.Success -> {
                        loadingEnable.postValue(false)
                        action.postValue(ACTION_SUCCESS)
                    }
                    is Resource.Error -> {
                        loadingEnable.postValue(false)
                        action.postValue(ACTION_ERROR)
                    }
                }
            }
        }
    }

    fun uploadLaporanAnonymous(
        namaJalan: String,
        image: String?,
        deskripsi: String,
        tipePengaduan: String,
        geometry: String,
        statusPengaduan: String
    ) {
        loadingEnable.value = true

        viewModelScope.launch {

            if (namaJalan.isEmpty() && deskripsi.isEmpty() && tipePengaduan.isEmpty()) {
                loadingEnable.postValue(false)
                action.postValue(ACTION_FAILED)
            } else {
                val masyarakat = DataPengaduanMasyarakat(
                    namaJalan,
                    image,
                    tipePengaduan,
                    deskripsi,
                    statusPengaduan,
                    geometry
                )
                when (repository.masyarakatLaporanAnonymouse(
                    masyarakat
                )) {
                    is Resource.Success -> {
                        loadingEnable.postValue(false)
                        action.postValue(ACTION_SUCCESS)
                    }
                    is Resource.Error -> {
                        loadingEnable.postValue(false)
                        action.postValue(ACTION_ERROR)
                    }
                }
            }
        }
    }

}