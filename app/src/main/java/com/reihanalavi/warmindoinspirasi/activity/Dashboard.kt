package com.reihanalavi.warmindoinspirasi.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reihanalavi.warmindoinspirasi.R
import com.reihanalavi.warmindoinspirasi.adapter.TransaksiAdapter
import com.reihanalavi.warmindoinspirasi.apiservice.ApiConfig
import com.reihanalavi.warmindoinspirasi.apiservice.ApiService
import com.reihanalavi.warmindoinspirasi.model.AktivitasPengguna
import com.reihanalavi.warmindoinspirasi.model.TransaksiModel
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Dashboard : AppCompatActivity() {

    lateinit var rv: RecyclerView
    lateinit var items: ArrayList<TransaksiModel>
    lateinit var adapter: TransaksiAdapter

    lateinit var btnRefresh: Button
    lateinit var btnLogout: Button
    lateinit var txtUsername: TextView
    lateinit var txtRole: TextView

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        items = arrayListOf()

        supportActionBar?.hide()

        rv = findViewById(R.id.rv_dashboard)
        rv.layoutManager = LinearLayoutManager(this)
        btnRefresh = findViewById(R.id.btn_refresh)
        btnLogout = findViewById(R.id.btn_logout)
        txtUsername = findViewById(R.id.txt_username)
        txtRole = findViewById(R.id.txt_role)

        Log.d("NAMAPENGGUNA DASHBOARD", intent.getStringExtra("namapengguna").toString())
        txtUsername.text = intent.getStringExtra("namapengguna")
        txtRole.text = intent.getStringExtra("role")

        sharedPref = this.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        btnRefresh.setOnClickListener {
            items.clear()
            adapter.notifyDataSetChanged()
            fetch(intent.getStringExtra("shift").toString())
        }

        adapter = TransaksiAdapter(this@Dashboard, items)
        rv.adapter = adapter

        Log.d("SHIFT", intent.getStringExtra("shift").toString())

        btnLogout.setOnClickListener {
            val editor = sharedPref.edit()

            editor.putString("username", "")
            editor.putString("role", "")
            editor.putString("namapengguna", "")

            editor.apply()

            postAktivitasPengguna(intent.getStringExtra("id").toString(), "logout")
        }

        fetch(intent.getStringExtra("shift").toString())
    }

    fun postAktivitasPengguna(id: String, aktivitas: String) {
        val apiConfig = ApiConfig.getService()
        val aktivitas = AktivitasPengguna(id, aktivitas)

        val postAktivitas = apiConfig.postAktivitasPengguna(aktivitas).enqueue(object : retrofit2.Callback<AktivitasPengguna> {
            override fun onResponse(
                call: Call<AktivitasPengguna>,
                response: Response<AktivitasPengguna>
            ) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
//                Log.d("RESPONSE", response.body().toString())
                if (response.isSuccessful) {
                    proceedLogout()

                }
            }

            override fun onFailure(call: Call<AktivitasPengguna>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }
        })
    }

    fun proceedLogout() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun fetch(shift: String) {
        val apiConfig = ApiConfig.getService()

        val transaksi = apiConfig.getTransaksi(""""shift"""", shift.toInt()).enqueue(object : retrofit2.Callback<Map<String, TransaksiModel>> {
            override fun onResponse(call: Call<Map<String, TransaksiModel>>, response: Response<Map<String, TransaksiModel>>) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
//                Log.d("RESPONSE", response.body().toString())
                if(response.isSuccessful) {
                    items.clear()
                    response.body()?.forEach { (key, value) ->
                        items.add(value)

                    }

                    Log.d("ITEMS", items.toString())
                    setToAdapter(items)

                }
            }

            override fun onFailure(call: Call<Map<String, TransaksiModel>>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
    }

    fun setToAdapter(data: ArrayList<TransaksiModel>) {
        adapter = TransaksiAdapter(this@Dashboard, data)
        rv.adapter = adapter

        adapter.notifyDataSetChanged()
    }
}