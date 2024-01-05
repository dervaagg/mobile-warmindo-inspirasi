package com.reihanalavi.warmindoinspirasi.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.reihanalavi.warmindoinspirasi.R
import com.reihanalavi.warmindoinspirasi.model.DetailTransaksiModel
import com.reihanalavi.warmindoinspirasi.utils.ItemClickListener

class DetailTransaksiAdapter(private val context: Context,
    private val detailTransaksi:List<DetailTransaksiModel>,
    private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<DetailTransaksiAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMenu: TextView = itemView.findViewById(R.id.items_menunama)
        val txtHarga: TextView = itemView.findViewById(R.id.items_menuharga)

        val btnToggle: Button = itemView.findViewById(R.id.items_toggle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_menu_detail, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return detailTransaksi.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val DetailTransaksi = detailTransaksi[position]

        holder.txtMenu.text = DetailTransaksi.namamenu
        holder.txtHarga.text = DetailTransaksi.jumlah.toString() + " x " + DetailTransaksi.harga.toString()


        if(DetailTransaksi.statusTransaksi != "Disajikan" && DetailTransaksi.statusTransaksi != "Selesai") {
            holder.btnToggle.visibility = View.VISIBLE
        } else {
            holder.btnToggle.visibility = View.GONE
        }

        if(DetailTransaksi.status == "Aktif") {
            holder.btnToggle.text = "Batal"
            holder.txtMenu.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.txtHarga.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            holder.btnToggle.text = "Aktif"
            holder.txtMenu.setTextColor(ContextCompat.getColor(context, R.color.disable))
            holder.txtHarga.setTextColor(ContextCompat.getColor(context, R.color.disable))
        }

        holder.btnToggle.setOnClickListener {
            itemClickListener.onItemClicked(DetailTransaksi)
        }
    }
}