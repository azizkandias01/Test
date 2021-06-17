package com.azizapp.test.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizapp.test.model.EditMasyarakatRequest
import com.azizapp.test.repository.MainRepository
import com.azizapp.test.ui.editpassword.EditPasswordViewModel
import com.azizapp.test.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    companion object {
        const val ACTION_EDIT_SUCCESS = "EDIT_SUCCESS"
        const val ACTION_EDIT_FAILED = "EDIT_FAILED"
        const val ACTION_EDIT_ERROR = "EDIT_ERROR"
    }

    val loadingEnable = MutableLiveData<Boolean>()
    var nama = MutableLiveData<String>()
    val noHp = MutableLiveData<String>()
    val emailUser = MutableLiveData<String>()
    val alamat = MutableLiveData<String>()
    val action = MutableLiveData<String>()

    val bearer: String = "Bearer " + com.azizapp.test.utill.Session.bearer

    fun onLoad() {
        viewModelScope.launch {
            when (val response = bearer.let { repository.getMasyarakatData(it) }) {
                is Resource.Success -> {
                    nama.postValue(response.data?.nama)
                    noHp.postValue(response.data?.noHp)
                    emailUser.postValue(response.data?.email)
                    alamat.postValue(response.data?.alamat)
                }
                is Resource.Error -> {
                    loadingEnable.postValue(false)
                }
            }
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val editMasyarakatRequest = EditMasyarakatRequest(
                nama = nama.value,
                noHp = noHp.value,
                alamat = alamat.value,
                email = emailUser.value,
                password = null,
                passwordConfirmation = null,
                foto = null,
                method = "PUT"
            )
            when (val responseEdit =
                bearer.let { repository.editMasyarakat(it, editMasyarakatRequest) }) {
                is Resource.Success -> {
                    if (responseEdit.data?.statusCode == 200) {
                        action.postValue(EditPasswordViewModel.ACTION_EDIT_SUCCESS)
                    } else {
                        action.postValue(EditPasswordViewModel.ACTION_EDIT_FAILED)
                    }
                }
                is Resource.Error -> {
                    action.postValue(EditPasswordViewModel.ACTION_EDIT_ERROR)
                }
            }
        }
    }

}

