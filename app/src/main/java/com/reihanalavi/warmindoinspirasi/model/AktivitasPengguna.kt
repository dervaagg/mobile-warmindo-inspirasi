package com.reihanalavi.warmindoinspirasi.model

import com.google.gson.annotations.SerializedName

data class AktivitasPengguna(
    @field:SerializedName("idpengguna") val idpengguna: String,
    @field:SerializedName("aktivitas") val aktivitas: String,
)
