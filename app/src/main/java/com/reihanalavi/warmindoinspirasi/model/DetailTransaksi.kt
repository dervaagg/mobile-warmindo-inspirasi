package com.reihanalavi.warmindoinspirasi.model

import com.google.gson.annotations.SerializedName

data class DetailTransaksiModel(
    var key: String,
    var statusTransaksi: String,
    @field:SerializedName("idmenu") val idmenu: String,
    @field:SerializedName("idtransaksi") val idtransaksi: String,
    @field:SerializedName("status") var status: String,
    @field:SerializedName("namamenu") val namamenu: String,
    @field:SerializedName("harga") val harga: Int,
    @field:SerializedName("jumlah") var jumlah: Int,
    @field:SerializedName("subtotal") val subtotal: Long,
)

data class ToggleStatus(
    @field:SerializedName("status") var status: String,
)