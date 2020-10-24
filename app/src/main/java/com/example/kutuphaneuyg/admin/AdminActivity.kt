package com.example.kutuphaneuyg.admin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.kutuphaneuyg.LoadingScreen
import com.example.kutuphaneuyg.LoginActivity
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loading : LoadingScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        loading = LoadingScreen(this)

        val adminMenu = ArrayList<String>()

        adminMenu.add("Kitap Ekle")
        adminMenu.add("Mevcut Kitaplar")
        adminMenu.add("Müşterideki Kitaplar")
        adminMenu.add("Tüm Kullanıcılar")
        adminMenu.add("Yöneticiler")
        adminMenu.add("Yönetici Ekle")
        adminMenu.add("Bilgilerim")
        adminMenu.add("Çıkış")

        listViewAdminPage.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,adminMenu) //simple_dropdown_item_1line

        listViewAdminPage.setOnItemClickListener { adapterView, view, i, l ->
            when(i){
                0 -> startActivity(Intent(applicationContext,BookAddActivity::class.java))
                1 -> startActivity(Intent(applicationContext,CurrentBooksActivity::class.java))
                2 -> startActivity(Intent(applicationContext,NotCurrentBooksActivity::class.java))
                3 -> startActivity(Intent(applicationContext,AllUsersActivity::class.java))
                4 -> startActivity(Intent(applicationContext,ManagerListActivity::class.java))
                5 -> startActivity(Intent(applicationContext,ManagerAddActivity::class.java))
                6 -> adminInfo()
                7 -> cikis()
            }
        }

    }

    private fun adminInfo() {
        loading.showLoading()

        db.collection("Yöneticiler").document(auth.currentUser!!.email.toString()).addSnapshotListener { doc, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(
                    applicationContext,
                    firebaseFirestoreException.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (doc != null) {
                    val sicilNo = doc.get("sicil_no") as Long
                    val email = doc.get("email") as String
                    val kayitTarih = doc.get("kayit_tarih") as Timestamp

                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Bilgileriniz")
                    alert.setMessage("Sicil No: " + sicilNo + "\nE-Mail: " + email + "\nKayit Tarih: " + kayitTarih.toDate())
                    alert.setPositiveButton("Tamam") { dialogInterface, i ->
                    }
                    alert.show()
                    loading.hideLoading()
                }
            }
        }
    }

    fun cikis() {

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Çıkış Yapmak Üzeresiniz")
        alert.setIcon(R.drawable.icon_power)
        alert.setPositiveButton("Onayla"){dialogInterface, i ->

            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        alert.setNegativeButton("Hayır"){dialogInterface, i ->
        }
        alert.show()

    }

    fun yoneticiList (view: View) {
        startActivity(Intent(applicationContext,ManagerListActivity::class.java))
    }

    fun yoneticiEkle(view: View) {
        startActivity(Intent(applicationContext,ManagerAddActivity::class.java))
    }

    fun goAllUser(view: View) {
        startActivity(Intent(applicationContext,AllUsersActivity::class.java))
    }

    fun goCurrentBooks(view: View) {
        startActivity(Intent(applicationContext,CurrentBooksActivity::class.java))
    }

    fun goNotCurrentBooks(view: View) {
        startActivity(Intent(applicationContext,NotCurrentBooksActivity::class.java))
    }

    fun kitapEkle (view: View) {
        startActivity(Intent(applicationContext,BookAddActivity::class.java))
    }

}