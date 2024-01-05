package com.reihanalavi.warmindoinspirasi.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.reihanalavi.warmindoinspirasi.R
import com.reihanalavi.warmindoinspirasi.activity.DetailActivity
import com.reihanalavi.warmindoinspirasi.apiservice.ApiConfig
import com.reihanalavi.warmindoinspirasi.model.DetailTransaksiModel
import com.reihanalavi.warmindoinspirasi.model.TransaksiModel
import retrofit2.Call
import retrofit2.Response

class TransaksiAdapter(
    private val context: Context,
    private val transaksi: ArrayList<TransaksiModel>) :
    RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMeja: TextView = itemView.findViewById(R.id.items_meja)
        val txtMenu: TextView = itemView.findViewById(R.id.items_menunama)
        val txtStatus: TextView = itemView.findViewById(R.id.items_status)

        val txtDetail: TextView = itemView.findViewById(R.id.items_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_transaksi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transaksi.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Transaksi = transaksi[position]

        holder.txtMeja.text = Transaksi.kodemeja
        holder.txtMenu.text = context.getString(R.string.tunggu_sebentar)
        holder.txtStatus.text = Transaksi.status

        when(Transaksi.status) {
            "Baru" -> { toggleColor(holder.txtStatus, R.drawable.baru_selected) }
            "Diproses" -> { toggleColor(holder.txtStatus, R.drawable.diproses_selected) }
            "Disajikan" -> { toggleColor(holder.txtStatus, R.drawable.disajikan_selected) }
            "Selesai" -> { toggleColor(holder.txtStatus, R.drawable.selesai_selected) }
        }

        holder.txtDetail.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("idpelanggan", Transaksi.idpelanggan)
            intent.putExtra("idpromosi", Transaksi.idpromosi)
            intent.putExtra("idtransaksi", Transaksi.idtransaksi)
            intent.putExtra("kodemeja", Transaksi.kodemeja)
            intent.putExtra("metodepembayaran", Transaksi.metodepembayaran)
            intent.putExtra("namapelanggan", Transaksi.namapelanggan)
            intent.putExtra("shift", Transaksi.shift)
            intent.putExtra("status", Transaksi.status)
            intent.putExtra("tanggal", Transaksi.tanggal)
            intent.putExtra("total", Transaksi.total)
            intent.putExtra("totaldiskon", Transaksi.totaldiskon)
            intent.putExtra("waktu", Transaksi.waktu)
            context.startActivity(intent)
        }

        val apiConfig = ApiConfig.getService()

        var items: ArrayList<DetailTransaksiModel> = arrayListOf()

        var namamenu: String = ""

        val detailTransaksi = apiConfig.getDetailTransaksi(""""idtransaksi"""", """"${Transaksi.idtransaksi}"""").enqueue(object : retrofit2.Callback<Map<String, DetailTransaksiModel>> {
            override fun onResponse(call: Call<Map<String, DetailTransaksiModel>>, response: Response<Map<String, DetailTransaksiModel>>) {
                Log.d("DETAIL CODE", response.code().toString())
                Log.d("DETAIL RAW", response.raw().toString())
                Log.d("DETAIL RESPONSE", response.body().toString())
                if(response.isSuccessful) {
                    items.clear()
                    response.body()?.forEach { (key, value) ->
                        Log.d("IDMENU [${key}]", value.idmenu.toString())
                        Log.d("NAMA MENU", value.namamenu)
                        items.add(value)
                        namamenu = namamenu + value.namamenu + " (" + value.jumlah + ")" + ", "
                    }

                }

                holder.txtMenu.text = namamenu.substring(0, namamenu.length-2)
            }

            override fun onFailure(call: Call<Map<String, DetailTransaksiModel>>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun toggleColor(text: TextView, drawable: Int) {
        val backgroundButton = ContextCompat.getDrawable(context, drawable)
        val textButton = ContextCompat.getColor(context, R.color.black)

        text.background = backgroundButton
        text.setTextColor(textButton)
    }
}