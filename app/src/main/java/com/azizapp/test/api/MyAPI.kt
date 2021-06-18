package com.azizapp.test.api

import com.azizapp.test.model.DataPengaduanMasyarakat
import com.azizapp.test.utill.Constant
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MyAPI {

//    @Multipart
    @POST("api/pengaduan")
    fun pengaduanMasyarakat(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String,
        @Body dataPengaduanMasyarakat: DataPengaduanMasyarakat
    ): Call<DataPengaduanMasyarakat>

    companion object {
        operator fun invoke(): MyAPI {
            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyAPI::class.java)
        }
    }

    @POST("api/pengaduan/anonim")
    fun pengaduanMasyarakatAnonim(
        @Header("Accept") accept: String,
//        @Part("nama_jalan") nama_jalan: RequestBody,
        //@Part image: MultipartBody.Part,
//        @Part("foto") foto : RequestBody,
//        @Part("tipe_pengaduan") tipe_pengaduan: RequestBody,
//        @Part("deskripsi_pengaduan") deskripsi_pengaduan: RequestBody,
//        @Part("geometry") geometry: RequestBody
        @Body dataPengaduanMasyarakat: DataPengaduanMasyarakat
    ): Call<DataPengaduanMasyarakat>

}