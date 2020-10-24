package com.example.kutuphaneuyg.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kutuphaneuyg.LoadingScreen
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manager_add.*

class ManagerAddActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loading : LoadingScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_add)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        loading = LoadingScreen(this)

    }

    fun managerAddButton (view : View) {

         val email = managerAddMail.text.toString()
         val password = managerAddSifre.text.toString()
         val sicil = managerAddSicilNo.text.toString()

        if(email.isEmpty() || password.isEmpty() || sicil.isEmpty())
            Toast.makeText(applicationContext,"Tüm Alanları Doldurmalısın", Toast.LENGTH_SHORT).show()
        else{
            loading.showLoading()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val managerRegister = hashMapOf<String,Any>()
                    managerRegister.put("sicil_no",sicil.toLong())
                    managerRegister.put("email",email)
                    managerRegister.put("sifre",password)
                    managerRegister.put("kayit_tarih", Timestamp.now())


                    db.collection("Yöneticiler").document(auth.currentUser!!.email.toString()).set(managerRegister).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            loading.hideLoading()
                            startActivity(Intent(applicationContext, AdminActivity::class.java))
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        loading.hideLoading()
                        Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener { exception ->
                if (exception != null)
                    loading.hideLoading()
                    Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

}