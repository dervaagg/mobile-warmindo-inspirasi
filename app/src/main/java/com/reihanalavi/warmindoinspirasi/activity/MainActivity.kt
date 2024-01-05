package com.reihanalavi.warmindoinspirasi.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.reihanalavi.warmindoinspirasi.R
import com.reihanalavi.warmindoinspirasi.apiservice.ApiConfig
import com.reihanalavi.warmindoinspirasi.model.AktivitasPengguna
import com.reihanalavi.warmindoinspirasi.model.LoginModel
import retrofit2.Call
import retrofit2.Response

class MainActivity : ComponentActivity() {
    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var btnLogin: Button

    lateinit var items: ArrayList<LoginModel>

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtEmail = findViewById(R.id.editText_email)
        edtPassword = findViewById(R.id.editText_password)
        btnLogin = findViewById(R.id.btn_login)

        items = arrayListOf()

        btnLogin.setOnClickListener {
            login(edtEmail.text.toString(), edtPassword.text.toString())
        }

        sharedPref = this.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val username = sharedPref.getString("username", "")
        val namapengguna = sharedPref.getString("namapengguna", "")
        val role = sharedPref.getString("role", "")
        val shift = sharedPref.getString("shift", "")

        if(username != "") {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("username", username)
            intent.putExtra("role", role)
            intent.putExtra("namapengguna", namapengguna)
            intent.putExtra("shift", shift)

            startActivity(intent)
        }
    }

    fun login(username: String, password: String) {
        val apiConfig = ApiConfig.getService()

        val loginInfo = apiConfig.getLogin(""""username"""", """"$username"""").enqueue(object : retrofit2.Callback<Map<String, LoginModel>> {
            override fun onResponse(call: Call<Map<String, LoginModel>>, response: Response<Map<String, LoginModel>>) {
//                Log.d("CODE", response.code().toString())
//                Log.d("RAW", response.raw().toString())
//                Log.d("RESPONSE", response.body().toString())
                if(response.isSuccessful) {
                    items.clear()
                    if(response.body()?.size == 0) {
                        Toast.makeText(this@MainActivity, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                    } else {
                        response.body()?.forEach { (key, value) ->
                            val itemModified = value
                            itemModified.id = key

                            items.add(itemModified)
                        }

                        checkCredentials(password)
                    }

                    Log.d("ITEMS", items.toString())

                }
            }

            override fun onFailure(call: Call<Map<String, LoginModel>>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }

        })
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
                    Log.d("ITEMS", items.toString())

                    proceedShift()

                }
            }

            override fun onFailure(call: Call<AktivitasPengguna>, t: Throwable) {
                Log.e("ERROR ON FAILURE", t.message.toString())
                t.printStackTrace()
            }
        })
    }

    fun proceedShift() {
        val intent = Intent(this, ShiftActivity::class.java)
        intent.putExtra("username", items[0].username)
        intent.putExtra("role", "Kitchen")
        intent.putExtra("namapengguna", items[0].namapengguna)
        intent.putExtra("id", items[0].id)

        startActivity(intent)
    }

    fun checkCredentials(password: String) {
        val passwordDb = items[0].password

        if(password == passwordDb) {
            val editor = sharedPref.edit()

            editor.putString("username", items[0].username)
            editor.putString("role", "Kitchen")
            editor.putString("namapengguna", items[0].namapengguna)
            editor.putString("id", items[0].id)

            editor.apply()

            postAktivitasPengguna(items[0].id, "login")
        } else {
            Toast.makeText(this@MainActivity, "Password salah", Toast.LENGTH_SHORT).show()
        }
    }
}