package com.reihanalavi.warmindoinspirasi.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    var id: String,
    @field:SerializedName("foto") val foto: String,
    @field:SerializedName("idpengguna") val kategori: Long,
    @field:SerializedName("idrole") val idrole: String,
    @field:SerializedName("namapengguna") val namapengguna: String,
    @field:SerializedName("password") var password: String,
    @field:SerializedName("status") var status: Boolean,
    @field:SerializedName("username") var username: String,
)
