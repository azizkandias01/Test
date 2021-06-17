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
class DetilRiwayatViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel() {
    val loadingEnable = MutableLiveData<Boolean>()
    val listPengaduan: ArrayList<Pengaduan> = arrayListOf()
    val action = MutableLiveData<String>()
    val actionItemPosition = MutableLiveData<Int>()

    companion object {
        const val ACTION_RIWAYAT_FETCHED = "RIWAYAT_FETCHED"
        const val ACTION_RIWAYAT_ONCLICK = "RIWAYAT_CLICKED"
    }

    fun onLoad() {
        val bearer: String? = "Bearer " + com.azizapp.test.utill.Session.bearer
        viewModelScope.launch {
            when (val response = bearer?.let { repository.getPengaduanMasyarakat(it) }) {
                is Resource.Success -> {
                    response.data?.forEach {
                        val pengaduan = Pengaduan(
                            deskripsiPengaduan = it.deskripsiPengaduan.toString(),
                            feedbackMasyarakat = it.feedbackMasyarakat.toString(),
                            foto = it.foto,
                            geometry = it.geometry,
                            id = it.id,
                            idAdmin = it.idAdmin,
                            idMasyarakat = it.idMasyarakat,
                            idPetugas = it.idPetugas.toString(),
                            laporanPetugas = it.laporanPetugas.toString(),
                            namaJalan = it.namaJalan.toString(),
                            statusPengaduan = it.statusPengaduan.toString(),
                            tipePengaduan = it.tipePengaduan.toString()
                        )

                        listPengaduan.add(pengaduan)
                    }
                    action.postValue(RiwayatViewModel.ACTION_RIWAYAT_FETCHED)
                }
                is Resource.Error -> {
                    loadingEnable.postValue(false)
                    // action.postValue(LoginViewModel.ACTION_LOGIN_ERROR)
                }
            }
        }
    }

    fun itemOnClick(position: Int) {
        actionItemPosition.value = position
        action.value = ACTION_RIWAYAT_ONCLICK
    }
}