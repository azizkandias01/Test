package com.azizapp.test.model


import com.google.android.gms.maps.model.LatLng
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

fun main() {
//    var test ="{\"type": "Point", "coordinates": [101.439627, 0.547225]}"
//    //print("{\"type\": \"Point\", \"coordinates\": ".length)
//    print(geoToLatLong(test))
}

fun geoToString(string: String): String {
    val substring = string.substring(34, string.length - 2)
    return substring
}

fun stringToLatLong(string: String): LatLng {
    val result = geoToString(string)
    val latlong: List<String> = result.split(", ")
    val lat = latlong[0].toDouble()
    val long = latlong[1].toDouble()
    return LatLng(lat, long)
}

fun geoToLatLong(string: String): LatLng {
    val substring = string.substring(34, string.length - 2)
    val latlong: List<String> = substring.split(",")
    val latLng = LatLng(latlong[1].toDouble(), latlong[0].toDouble())
    return latLng
}