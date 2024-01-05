package com.reihanalavi.warmindoinspirasi.model

import com.google.gson.annotations.SerializedName

data class TransaksiModel(
    @field:SerializedName("idpelanggan") val idpelanggan: Long,
    @field:SerializedName("idpromosi") val idpromosi: Int,
    @field:SerializedName("idtransaksi") val idtransaksi: String,
    @field:SerializedName("kodemeja") val kodemeja: String,
    @field:SerializedName("metodepembayaran") val metodepembayaran: String,
    @field:SerializedName("namapelanggan") val namapelanggan: Int,
    @field:SerializedName("shift") val shift: Int,
    @field:SerializedName("status") var status: String,
    @field:SerializedName("tanggal") val tanggal: String,
    @field:SerializedName("total") val total: Long,
    @field:SerializedName("totaldiskon") val totaldiskon: Long,
    @field:SerializedName("waktu") val waktu: String
)

data class UpdateStatusTransaksi(
    @field:SerializedName("status") var status: String,
)

data class FinishTransaksi(
    @field:SerializedName("status") var status: String,
    @field:SerializedName("metodepembayaran") val metodepembayaran: String,
    @field:SerializedName("totaldiskon") val totaldiskon: Long,
    @field:SerializedName("total") val total: Long,
)