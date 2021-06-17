package com.azizapp.test.model


import com.google.gson.annotations.SerializedName

data class Masyarakat(
    @SerializedName("alamat")
    var alamat: String? = "",
    @SerializedName("created_at")
    var createdAt: Any? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("foto")
    var foto: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("nama")
    var nama: String? = "",
    @SerializedName("no_hp")
    var noHp: String? = "",
    @SerializedName("updated_at")
    var updatedAt: Any? = "",
    @SerializedName("password")
    var password: String? = ""
)