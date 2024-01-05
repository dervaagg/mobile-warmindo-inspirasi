package com.reihanalavi.warmindoinspirasi.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reihanalavi.warmindoinspirasi.R
import com.reihanalavi.warmindoinspirasi.adapter.DetailTransaksiAdapter
import com.reihanalavi.warmindoinspirasi.adapter.TransaksiAdapter
import com.reihanalavi.warmindoinspirasi.apiservice.ApiConfig
import com.reihanalavi.warmindoinspirasi.model.DetailTransaksiModel
import com.reihanalavi.warmindoinspirasi.model.FinishTransaksi
import com.reihanalavi.warmindoinspirasi.model.MenuModel
import com.reihanalavi.warmindoinspirasi.model.ToggleStatus
import com.reihanalavi.warmindoinspirasi.model.TransaksiModel
import com.reihanalavi.warmindoinspirasi.model.UpdateStatusTransaksi
import com.reihanalavi.warmindoinspirasi.utils.ItemClickListener
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class DetailActivity : AppCompatActivity(), ItemClickListener {

    lateinit var rv: RecyclerView
    lateinit var items: ArrayList<DetailTransaksiModel>
    lateinit var adapter: DetailTransaksiAdapter

    lateinit var spStatus: Spinner
    lateinit var spMetode: Spinner

    lateinit var clPayment: CardView
    lateinit var clStatus: CardView

    lateinit var txtJumlah: TextView
    lateinit var txtSub: TextView
    lateinit var txtTotal: TextView

    lateinit var btnUpdate: Button
    lateinit var btnFinish: Button

    lateinit var item: TransaksiModel

    var selected: String = ""

    var jml: Int = 0
    var subtotal: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        items = arrayListOf()

        rv = findViewById(R.id.rv_detail)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = DetailTransaksiAdapter(this@DetailActivity, items, this)
//        adapter.itemClickListener = this
        rv.adapter = adapter

        clPayment = findViewById(R.id.cl_payment)
        clStatus = findViewById(R.id.cl_status)
        spStatus = findViewById(R.id.spinner_status)
        spMetode = findViewById(R.id.spinner_metode)
        txtJumlah = findViewById(R.id.txt_dyn_jumlah)
        txtSub = findViewById(R.id.txt_dyn_sub)
        txtTotal = findViewById(R.id.txt_dyn_total)
        btnFinish = findViewById(R.id.btn_finish)

        btnUpdate = findViewById(R.id.btn_update)

        if(intent.getStringExtra("status") == "Baru" || intent.getStringExtra("status") == "Diproses") {
            clStatus.visibility = View.VISIBLE
            clPayment.visibility = View.GONE

            btnFinish.visibility = View.VISIBLE
        } else {
            if(intent.getStringExtra("status") == "Disajikan") {
                clStatus.visibility = View.GONE
                clPayment.visibility = View.VISIBLE

                btnFinish.visibility = View.VISIBLE
                spMetode.isEnabled = true
            } else {
                clStatus.visibility = View.GONE
                clPayment.visibility = View.VISIBLE

                btnFinish.visibility = View.GONE
                spMetode.isEnabled = false
            }

        }

        val statusItems = listOf("Baru", "Diproses", "Disajikan")
        val metodeItems = listOf("QRIS", "Cash", "Debit")

        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusItems)
        val metodeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metodeItems)

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spStatus.adapter = statusAdapter
        spMetode.adapter = metodeAdapter

        Log.d("METODE", intent.getStringExtra("metodepembayaran").toString())
        spStatus.setSelection(statusItems.indexOf(intent.getStringExtra("status")))
        spMetode.setSelection(metodeItems.indexOf(intent.getStringExtra("metodepembayaran")))

        selected = intent.getStringExtra("status").toString()
        btnUpdate.setOnClickListener {
            updateStatus(selected, intent.getStringExtra("idtransaksi").toString())
        }
        btnFinish.setOnClickListener {
            val transaksi = FinishTransaksi(
                "Selesai",
                spMetode.selectedItem.toString(),
                subtotal,
                subtotal
            )

            finishTransaksi(intent.getStringExtra("idtransaksi").toString(), transaksi)
        }

//        if(selected == spStatus.selectedItem.toString()) {
//            btnUpdate.isEnabled = false
//        } else {
//            btnUpdate.isEnabled = true
//        }

        item = TransaksiModel(
            intent.getLongExtra("idpelanggan", 0),
            intent.getIntExtra("idpromosi", 0),
            intent.getStringExtra("idtransaksi").toString(),
            intent.getStringExtra("kodemeja").toString(),
            intent.getStringExtra("metodepembayaran").toString(),
            intent.getIntExtra("namapelanggan", 0),
            intent.getIntExtra("shift", 0),
            intent.getStringExtra("status").toString(),
            intent.getStringExtra("tanggal").toString(),
            intent.getLongExtra("total", 0),
            intent.getLongExtra("totaldiskon", 0),
            intent.getStringExtra("waktu").toString()
        )

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected = p0?.getItemAtPosition(p2).toString()
                btnUpdate.isEnabled = true
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        fetch()
    }

    fun finishTransaksi(id: String, transaksi: FinishTransaksi) {
        val apiConfig = ApiConfig.getService()

        val updateTransaksi = apiConfig.finishTransaksi(Uri.encode(id), transaksi).enqueue(object : retrofit2.Callback<FinishTransaksi> {
            override fun onResponse(call: Call<FinishTransaksi>, response: Response<FinishTransaksi>) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
//                Log.d("RESPONSE", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(this@DetailActivity, "Berhasil menyelesaikan transaksi", Toast.LENGTH_SHORT).show()

                    val resultIntent = Intent()
                    setResult(RESULT_OK, resultIntent)
                    finish()
                    onBackPressedDispatcher.onBackPressed()
                }

            }

            override fun onFailure(call: Call<FinishTransaksi>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun updateStatus(state: String, id: String) {
        val apiConfig = ApiConfig.getService()

        item.status = state
        val updateItem: UpdateStatusTransaksi = UpdateStatusTransaksi(state)

        val updateTransaksi = apiConfig.updateTransaksi(Uri.encode(id), updateItem).enqueue(object : retrofit2.Callback<UpdateStatusTransaksi> {
            override fun onResponse(call: Call<UpdateStatusTransaksi>, response: Response<UpdateStatusTransaksi>) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
//                Log.d("RESPONSE", response.body().toString())
                if(response.isSuccessful) {
                    Toast.makeText(this@DetailActivity, "Berhasil mengubah menjadi $state", Toast.LENGTH_SHORT).show()

                    val resultIntent = Intent()
                    setResult(RESULT_OK, resultIntent)
                    finish()
                    onBackPressedDispatcher.onBackPressed()
                }

            }

            override fun onFailure(call: Call<UpdateStatusTransaksi>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun fetch() {
        val apiConfig = ApiConfig.getService()

        val detailTransaksi = apiConfig.getDetailTransaksi(""""idtransaksi"""", """"${intent.getStringExtra("idtransaksi")}"""").enqueue(object : retrofit2.Callback<Map<String, DetailTransaksiModel>> {
            override fun onResponse(call: Call<Map<String, DetailTransaksiModel>>, response: Response<Map<String, DetailTransaksiModel>>) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
//                Log.d("RESPONSE", response.body().toString())
                if(response.isSuccessful) {
                    jml = 0
                    subtotal = 0

                    items.clear()
                    response.body()?.forEach() { (key, value) ->
                        var itemModified = value
                        itemModified.key = key
                        itemModified.statusTransaksi = intent.getStringExtra("status").toString()

                        Log.d("ITEM MAKANAN ID DOKUMEN", itemModified.key)

                        items.add(itemModified)
                        if(value.status == "Aktif") {
                            jml += value.jumlah
                            subtotal += (value.harga * value.jumlah)
                        }
                    }

                    Log.d("ITEMS", items.toString())
                    setToAdapter(items)

                }

                txtSub.text = subtotal.toString()
                txtTotal.text = subtotal.toString()
                txtJumlah.text = jml.toString()
            }

            override fun onFailure(call: Call<Map<String, DetailTransaksiModel>>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun setToAdapter(data: ArrayList<DetailTransaksiModel>) {
        adapter = DetailTransaksiAdapter(this@DetailActivity, data, this)
        rv.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    override fun onItemClicked(item: DetailTransaksiModel) {

        Log.d("CLICKED", "OK")
        Log.d("ID TRANSAKSI MENU", item.key)

        val apiConfig = ApiConfig.getService()
        var state = ""

        state = if(item.status == "Aktif") {
            "Batal"
        } else {
            "Aktif"
        }

        val toggle = ToggleStatus(state)

        val toggleStatusRequest = apiConfig.toggleStatusDetailTransaksi(Uri.encode(item.key), toggle).enqueue(object : retrofit2.Callback<ToggleStatus> {
            override fun onResponse(call: Call<ToggleStatus>, response: Response<ToggleStatus>) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
                Log.d("RESPONSE AFTER UPDATE", response.body().toString())
                if(response.isSuccessful) {
                    Log.d("SUCCESS", "OK")
                    fetch()
                }
            }

            override fun onFailure(call: Call<ToggleStatus>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
    }
}