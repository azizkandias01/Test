package com.azizapp.test.ui.daftar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizapp.test.model.MasyarakatDaftar
import com.azizapp.test.repository.MainRepository
import com.azizapp.test.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DaftarViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    companion object {
        const val ACTION_DAFTAR_SUCCESS = "DAFTAR_SUCCESS"
        const val ACTION_DAFTAR_FAILED = "DAFTAR_FAILED"
        const val ACTION_DAFTAR_ERROR = "DAFTAR_ERROR"
    }


    val loadingEnable = MutableLiveData<Boolean>()
    val tvNama = MutableLiveData<String>()
    val tvEmail = MutableLiveData<String>()
    val tvPassword = MutableLiveData<String>()
    val tvNoHp = MutableLiveData<String>()
    val tvPasswordConfirmation = MutableLiveData<String>()
    val action = MutableLiveData<String>()

    fun btnDaftar_Click() {
        loadingEnable.value = true
        viewModelScope.launch {
            if (!tvNama.value.isNullOrEmpty() && !tvEmail.value.isNullOrEmpty() && !tvPassword.value.isNullOrEmpty() && !tvPasswordConfirmation.value.isNullOrEmpty() && !tvNoHp.value.isNullOrEmpty()) {
                val masyarakatDaftar = MasyarakatDaftar(
                    email = tvEmail.value,
                    password = tvPassword.value,
                    password_confirmation = tvPasswordConfirmation.value,
                    nama = tvNama.value,
                    noHp = tvNoHp.value,
                    foto = null,
                    alamat = "NOT_DEFINED_YET"
                )
                when (val response = repository.masyarakatDaftar(masyarakatDaftar)) {
                    is Resource.Success -> {
                        if (response.data?.status_code == "201") {
                            loadingEnable.postValue(false)
                            action.postValue(ACTION_DAFTAR_SUCCESS)
                        } else {
                            loadingEnable.postValue(false)
                            action.postValue(ACTION_DAFTAR_FAILED)
                        }
                    }
                    is Resource.Error -> {
                        loadingEnable.postValue(false)
                        action.postValue(ACTION_DAFTAR_ERROR)
                    }
                }
            } else {
                loadingEnable.postValue(false)
                action.postValue(ACTION_DAFTAR_ERROR)
            }
        }
    }
}
