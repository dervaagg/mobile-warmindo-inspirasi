package com.reihanalavi.warmindoinspirasi.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.reihanalavi.warmindoinspirasi.R
import com.reihanalavi.warmindoinspirasi.apiservice.ApiConfig
import com.reihanalavi.warmindoinspirasi.model.AktivitasPengguna
import retrofit2.Call
import retrofit2.Response

class ShiftActivity : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var button: Button
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift)

        spinner = findViewById(R.id.spinner_shift)
        button = findViewById(R.id.btn_masuk)

        val items = listOf("Shift 1", "Shift 2")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        sharedPref = this.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        button.setOnClickListener {
            val editor = sharedPref.edit()

//            editor.putString("username", intent.getStringExtra("username"))
//            editor.putString("role", intent.getStringExtra("role"))
//            editor.putString("namapengguna", intent.getStringExtra("namapengguna"))
            editor.putString("shift", (spinner.selectedItemPosition + 1).toString())

            editor.apply()

            Log.d("NAMAPENGGUNA", intent.getStringExtra("namapengguna").toString())

            postAktivitasPengguna(intent.getStringExtra("id").toString(), "akses shift")
        }
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
                    proceedDashboard()

                }
            }

            override fun onFailure(call: Call<AktivitasPengguna>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }
        })
    }

    fun proceedDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        intent.putExtra("username", sharedPref.getString("username", ""))
        intent.putExtra("role", sharedPref.getString("role", ""))
        intent.putExtra("namapengguna", sharedPref.getString("namapengguna", ""))
        intent.putExtra("shift", (spinner.selectedItemPosition + 1).toString())
        intent.putExtra("id", sharedPref.getString("id", ""))

        startActivity(intent)
    }
}