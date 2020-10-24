package com.example.kutuphaneuyg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kutuphaneuyg.user.UserActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.editTextMail

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var loading : LoadingScreen


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        loading = LoadingScreen(this)

    }

    fun signUP(view: View){

        email = editTextMail.text.toString()
        password = editTextPass1.text.toString()

        if(kayitKontrol1()){ // tum alanlar dolu ve kayıt ıcın herhangı bır problem yok ıse

            loading.showLoading()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    veritabaniKayit()
                }
            }.addOnFailureListener { exception ->
                loading.hideLoading()
                Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun kayitKontrol1() : Boolean {

        if(editTextName.text.toString().isEmpty() || editTextSurname.text.toString().isEmpty() || email.isEmpty() || password.isEmpty() || editTextPass2.text.toString().isEmpty() || editTextNo.text.toString().isEmpty() ){
            Toast.makeText(applicationContext,"Tüm Alanları Doldurmalısın",Toast.LENGTH_SHORT).show()
            return false
        }

        else if(editTextNo.text.toString().length < 10) {
            Toast.makeText(applicationContext,"Telefon En Az 10 Karakter İçermeli",Toast.LENGTH_SHORT).show()

            return false
        }

        else if(editTextPass1.text.toString().length < 6) {
            Toast.makeText(applicationContext,"Şifre En Az 6 Karakter İçermeli",Toast.LENGTH_SHORT).show()

            editTextPass1.text?.clear();  editTextPass2.text?.clear()

            return false
        }

        else if(editTextPass1.text.toString() != editTextPass2.text.toString() ){
            Toast.makeText(applicationContext,"Girdiğiniz Şifreler Birbirleri İle Farklı. Şifrenizi  Tekrar Girin",Toast.LENGTH_SHORT).show()

            editTextPass1.text?.clear();  editTextPass2.text?.clear()

            return false
        }

        else
            return true

    }

    fun veritabaniKayit() {

        val currentMail = auth.currentUser!!.email.toString()

        val userRegister = hashMapOf<String,Any>()
        userRegister.put("ad",editTextName.text.toString())
        userRegister.put("soyad",editTextSurname.text.toString())
        userRegister.put("telefon",editTextNo.text.toString())
        userRegister.put("email",currentMail)
        userRegister.put("sifre",editTextPass1.text.toString())
        userRegister.put("kayit_tarih",Timestamp.now())

        db.collection("Kullanıcılar").document(currentMail).set(userRegister).addOnCompleteListener { task ->
            if(task.isSuccessful){
                loading.hideLoading()
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }
        }.addOnFailureListener { exception ->
            loading.hideLoading()
            Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
        }


    }


}