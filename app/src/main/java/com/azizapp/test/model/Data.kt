package com.azizapp.test.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("alamat")
    var alamat: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("foto")
    var foto: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("nama")
    var nama: String?,
    @SerializedName("no_hp")
    var noHp: String?,
    @SerializedName("updated_at")
    var updatedAt: String?
)

fun geoToString(string: String): String {
    return string.substring(34, string.length - 2)
}
