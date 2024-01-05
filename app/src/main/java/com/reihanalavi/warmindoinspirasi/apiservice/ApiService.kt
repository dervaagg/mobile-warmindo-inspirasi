package com.reihanalavi.warmindoinspirasi.apiservice

import com.reihanalavi.warmindoinspirasi.model.AktivitasPengguna
import com.reihanalavi.warmindoinspirasi.model.DetailTransaksiModel
import com.reihanalavi.warmindoinspirasi.model.FinishTransaksi
import com.reihanalavi.warmindoinspirasi.model.LoginModel
import com.reihanalavi.warmindoinspirasi.model.MenuModel
import com.reihanalavi.warmindoinspirasi.model.ToggleStatus
import com.reihanalavi.warmindoinspirasi.model.TransaksiModel
import com.reihanalavi.warmindoinspirasi.model.UpdateStatusTransaksi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("warmindo_inspirasi/transaksi.json") fun getTransaksi(@Query("orderBy") order: String, @Query("equalTo") shift: Int): Call<Map<String, TransaksiModel>>
    @GET("warmindo_inspirasi/detailtransaksi.json?") fun getDetailTransaksi(@Query("orderBy") order: String, @Query("equalTo") id: String): Call<Map<String, DetailTransaksiModel>>
    @GET("warmindo_inspirasi/pengguna.json?") fun getLogin(@Query("orderBy") order: String, @Query("equalTo") username: String): Call<Map<String, LoginModel>>
    @PATCH ("warmindo_inspirasi/transaksi/{id}.json") fun updateTransaksi(@Path("id") id: String, @Body data:UpdateStatusTransaksi): Call<UpdateStatusTransaksi>
    @PATCH ("warmindo_inspirasi/transaksi/{id}.json") fun finishTransaksi(@Path("id") id: String, @Body data:FinishTransaksi): Call<FinishTransaksi>
    @PATCH("warmindo_inspirasi/detailtransaksi/{id}.json") fun toggleStatusDetailTransaksi(@Path("id") id: String, @Body data:ToggleStatus): Call<ToggleStatus>
    @POST("warmindo_inspirasi/aktivitaspengguna.json") fun postAktivitasPengguna(@Body data:AktivitasPengguna): Call<AktivitasPengguna>
}
