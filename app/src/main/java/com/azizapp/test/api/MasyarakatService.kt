package com.azizapp.test.api

import com.azizapp.test.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

//define service class


interface MasyarakatService {

    @POST("api/login/masyarakat")
    suspend fun loginMasyarakat(
        @Body masyarakat: Masyarakat
    ): Response<MasyarakatLoginResponse>

    @GET("api/masyarakat/profile")
    suspend fun profileMasyarakat(
        @Header("Authorization") token: String
    ): Response<ProfileMasyarakat>

    @POST("api/register/masyarakat")
    suspend fun daftarMasyarakat(
        @Header("Accept") accept: String,
        @Body masyarakatDaftar: MasyarakatDaftar
    ): Response<MasyarakatDaftarResponse>

    @Multipart
    @POST("api/pengaduan")
    suspend fun pengaduanMasyarakat(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String,
        @Part ("nama_jalan") nama_jalan: RequestBody,
        @Part image: MultipartBody.Part,
        @Part ("tipe_pengaduan") tipe_pengaduan: RequestBody,
        @Part ("deskripsi_pengaduan") deskripsi_pengaduan: RequestBody,
        @Part ("geometry") geometry: RequestBody
    ): Response<DataPengaduanMasyarakat>

    @POST("api/masyarakat")
    suspend fun editPassword(
        @Header("Authorization") token: String,
        @Body editMasyarakatRequest: EditMasyarakatRequest
    ): Response<EditMasyarakatResponse>

    @GET("api/pengaduan_by_masyarakat")
    suspend fun riwayatMasyarakat(
        @Header("Authorization") token: String
    ):Response<PengaduanResponse>

    @GET("api/titik_banjir")
    suspend fun titikBanjir():Response<TitikBanjirResponse>

    @GET("api/titik_tersumbat")
    suspend fun titikTersumbat():Response<TitikTersumbatResponse>
}