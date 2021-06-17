package com.azizapp.test.model


import com.google.gson.annotations.SerializedName

data class MasyarakatDaftar(
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("password")
    var password: String? = "",
    @SerializedName("password_confirmation")
    var password_confirmation: String? = "",
    @SerializedName("nama")
    var nama: String? = "",
    @SerializedName("no_hp")
    var noHp: String? = "",
    @SerializedName("foto")
    var foto: String? = "",
    @SerializedName("alamat")
    var alamat: String? = ""
)
