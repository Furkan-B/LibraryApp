package com.example.kutuphaneuyg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var loading : LoadingScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()
        loading = LoadingScreen(this)

    }

    fun newPass (view : View) {

        val mail = forgotPassMail.text.toString()

        if( mail.isEmpty() )
            Toast.makeText(applicationContext,"E-Mail Giriniz", Toast.LENGTH_SHORT).show()
        else{
            loading.showLoading()

            auth.sendPasswordResetEmail(mail).addOnSuccessListener {
                loading.hideLoading()
                Toast.makeText(applicationContext,"Yeni Şifre İçin Mail Gönderdik", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_SHORT).show()
                loading.hideLoading()
            }

        }
    }

}