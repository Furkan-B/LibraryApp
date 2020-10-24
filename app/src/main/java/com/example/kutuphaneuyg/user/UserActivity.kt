package com.example.kutuphaneuyg.user

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.kutuphaneuyg.LoginActivity
import com.example.kutuphaneuyg.R
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }

    fun myProfil (view: View) {
        startActivity(Intent(this, AccountActivity::class.java))
    }

    fun signOut (view: View) {

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Çıkış Yapmak Üzeresiniz")
        alert.setIcon(R.drawable.icon_power)
        alert.setPositiveButton("Onayla"){dialogInterface, i ->

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        alert.setNegativeButton("Hayır"){dialogInterface, i ->
        }
        alert.show()

    }

    fun kitapAL(view : View) {
        startActivity(Intent(applicationContext,BooksActivity::class.java))
    }

    fun teslimEdilecekKitaplar(view : View) {
        startActivity(Intent(applicationContext,BooksOfCurrentUserActivity::class.java))
    }

    fun eskiKitaplar (view: View) {
        startActivity(Intent(applicationContext,OldBooksActivity::class.java))
    }

}