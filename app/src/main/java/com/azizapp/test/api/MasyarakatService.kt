package com.azizapp.test.api

import com.azizapp.test.model.*
import retrofit2.Response
import retrofit2.http.*

//define service class


interface MasyarakatService {

    @POST("api/login/masyarakat")
    suspend fun loginMasyarakat(
        @Body masyarakat:Masyarakat
    ):Response<MasyarakatLoginResponse>

    @GET("api/masyarakat/profile")
    suspend fun profileMasyarakat(
        @Header("Authorization") token: String
    ):Response<ProfileMasyarakat>

    @POST("api/register/masyarakat")
    suspend fun daftarMasyarakat(
        @Body masyarakatDaftar: MasyarakatDaftar
    ):Response<MasyarakatDaftarResponse>

    @POST("/api/pengaduan")
    suspend fun pengaduanMasyarakat(
        @Header("Authorization") token: String,
        @Body dataPengaduan: DataPengaduan
    ) : Response<MasyarakatDaftarResponse>

    @GET("api/pengaduan_by_masyarakat")
    suspend fun riwayatMasyarakat(
        @Header("Authorization") token: String
    ):Response<PengaduanResponse>
}