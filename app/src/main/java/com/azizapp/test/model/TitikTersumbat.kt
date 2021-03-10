package com.azizapp.test.model


import com.google.gson.annotations.SerializedName

data class TitikTersumbat(
    @SerializedName("foto")
    var foto: String,
    @SerializedName("geometry")
    var geometry: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("keterangan")
    var keterangan: String,
    @SerializedName("nama_jalan")
    var namaJalan: String,
    @SerializedName("status")
    var status: String?
)