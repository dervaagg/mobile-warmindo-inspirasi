package com.reihanalavi.warmindoinspirasi.model

import com.google.gson.annotations.SerializedName

data class MenuModel(
    @field:SerializedName("idmenu") val idmenu: String,
    @field:SerializedName("kategori") val kategori: String,
    @field:SerializedName("namamenu") val namamenu: String,
    @field:SerializedName("harga") val harga: Int,
    @field:SerializedName("gambar") var gambar: Int,
)
