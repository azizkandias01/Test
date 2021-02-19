package com.azizapp.test.repository

import com.azizapp.test.api.MasyarakatService
import com.azizapp.test.model.*
import com.azizapp.test.utill.Resource

import javax.inject.Inject

class MainRepository @Inject constructor(
    private val masyarakatService: MasyarakatService
) {

    suspend fun masyarakatLogin(masyarakat: Masyarakat): Resource<MasyarakatLoginResponse> {
        masyarakatService.loginMasyarakat(masyarakat).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { return Resource.Success(it) }
            }
            return Resource.Error(response.message())
        }
    }

    suspend fun getMasyarakatData(bearer : String) : Resource<ProfileMasyarakat>{
        masyarakatService.profileMasyarakat(bearer).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { return Resource.Success(it) }
            }
            return Resource.Error(response.message())
        }
    }

    suspend fun masyarakatDaftar(masyarakatDaftar: MasyarakatDaftar) : Resource<MasyarakatDaftarResponse>{
        masyarakatService.daftarMasyarakat(masyarakatDaftar).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { return Resource.Success(it) }
            }
            return Resource.Error(response.message())
        }
    }

    suspend fun getPengaduanMasyarakat(bearer : String) : Resource<PengaduanResponse>{
        masyarakatService.riwayatMasyarakat(bearer).let { response ->
            if (response.isSuccessful) {
                response.body()?.let { return Resource.Success(it) }
            }
            return Resource.Error(response.message())
        }
    }

}