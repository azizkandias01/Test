package com.azizapp.test.model


import com.google.gson.annotations.SerializedName

data class DataPengaduanMasyarakat(
    @SerializedName("nama_jalan")
    var namaJalan: String? = "",
    @SerializedName("foto")
    var foto: String? = "UNDEFINED",
    @SerializedName("tipe_pengaduan")
    var tipePengaduan: String? = "Banjir",
    @SerializedName("deskripsi_pengaduan")
    var deskripsiPengaduan: String? = "",
    @SerializedName("status_pengaduan")
    var statusPengaduan: String? = "Belum diverifikasi",
    @SerializedName("geometry")
    var geometry: String?
)