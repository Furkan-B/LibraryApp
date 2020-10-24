package com.example.kutuphaneuyg.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kutuphaneuyg.LoadingScreen
import com.example.kutuphaneuyg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.editTextMail

class AccountActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loading : LoadingScreen
    lateinit var ad : String; lateinit var soyad : String; lateinit var email : String; lateinit var telefon : String
    var kontrol = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        loading = LoadingScreen(this)

        db.collection("Kullanıcılar").document(auth.currentUser!!.email.toString()).addSnapshotListener { doc, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            else{
                if (doc != null) {
                    ad = doc.get("ad") as String
                    soyad = doc.get("soyad") as String
                    email = doc.get("email") as String
                    telefon = doc.get("telefon") as String

                    editTextName.setText(ad);editTextName.isEnabled = false
                    editTextSurname.setText(soyad);editTextSurname.isEnabled =false
                    editTextNo.setText(telefon);editTextNo.isEnabled = false
                    editTextMail.setText(email);editTextMail.isEnabled = false
                    editTextPass1.setText("**********");editTextPass1.isEnabled = false
                    textInputLayout7.setVisibility(View.INVISIBLE);
                    button4.setText("Bilgilerimi Güncelle")//button4.setVisibility(View.INVISIBLE);
                }
            }
        }

        button4.setOnClickListener {
            if(kontrol){
                editTextName.isEnabled = true
                editTextSurname.isEnabled =true
                editTextNo.isEnabled = true
                editTextPass1.text?.clear();editTextPass1.isEnabled = true
                textInputLayout7.setVisibility(View.VISIBLE);editTextPass2.text?.clear()
                button4.setText("Güncellemeleri Kaydet")
                kontrol=false
            }
            else{
                if(kayitKontrol1()){
                    loading.showLoading()
                    veritabaniGuncelle()
                }
            }
        }
    }

    private fun veritabaniGuncelle() {

        val currentMail = auth.currentUser!!.email.toString()

        val userRegister = hashMapOf<String,Any>()
        userRegister.put("ad",editTextName.text.toString())
        userRegister.put("soyad",editTextSurname.text.toString())
        userRegister.put("telefon",editTextNo.text.toString())
        userRegister.put("sifre",editTextPass1.text.toString())


        auth.currentUser!!.updatePassword(editTextPass1.text.toString().trim()).addOnSuccessListener {
            db.collection("Kullanıcılar").document(currentMail).update(userRegister).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Güncelleme Başarılı",Toast.LENGTH_SHORT).show()
                    loading.hideLoading()
                    kontrol=true
                    fun1()
                }
            }.addOnFailureListener { exception ->
                loading.hideLoading()
                Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            loading.hideLoading()
            println(exception.localizedMessage)
            Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
        }



    }

    private fun fun1() {
        editTextName.setText(ad);editTextName.isEnabled = false
        editTextSurname.setText(soyad);editTextSurname.isEnabled =false
        editTextNo.setText(telefon);editTextNo.isEnabled = false
        editTextMail.setText(email);editTextMail.isEnabled = false
        editTextPass1.setText("**********");editTextPass1.isEnabled = false
        textInputLayout7.setVisibility(View.INVISIBLE);
        button4.setText("Bilgilerimi Güncelle")//button4.setVisibility(View.INVISIBLE);
    }


    fun kayitKontrol1() : Boolean {

        if(editTextName.text.toString().isEmpty() || editTextSurname.text.toString().isEmpty() || editTextName.text.toString().isEmpty() || editTextPass1.text.toString().isEmpty() || editTextPass2.text.toString().isEmpty() || editTextNo.text.toString().isEmpty() ){
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

    override fun onBackPressed() {
        startActivity(Intent(this,UserActivity::class.java))
        finish()
    }

}