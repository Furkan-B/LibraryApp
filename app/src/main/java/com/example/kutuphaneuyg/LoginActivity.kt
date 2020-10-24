package com.example.kutuphaneuyg

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kutuphaneuyg.admin.AdminActivity
import com.example.kutuphaneuyg.user.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextMail

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loading : LoadingScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        loading = LoadingScreen(this)

    }

    fun goPass (view: View) {
        startActivity(Intent(applicationContext,ForgotPasswordActivity::class.java))
    }

    fun goSignUp(view : View){
        startActivity(Intent(applicationContext,SignUpActivity::class.java))
    }


    fun signIn(view: View){

        if( editTextMail.text.toString().isEmpty() || editTextPass.text.toString().isEmpty() )
            Toast.makeText(applicationContext,"Tüm Alanları Doldurmalısın",Toast.LENGTH_SHORT).show()
        else{
            loading.showLoading()

            auth.signInWithEmailAndPassword(editTextMail.text.toString(),editTextPass.text.toString()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    whoAmI()
                }
            }.addOnFailureListener { exception ->
                loading.hideLoading()
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun whoAmI () {

        val currentMail = auth.currentUser!!.email.toString()

        db.collection("Yöneticiler").document(currentMail).get().addOnSuccessListener { snapshot ->
            if (snapshot != null && snapshot.exists()) {                  // yönetici
                loading.hideLoading()
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            } else {                                                                             // kullanıcı

                db.collection("Kullanıcılar").document(currentMail).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot != null && snapshot.exists()) {    //hesap aktif
                            loading.hideLoading()
                            startActivity(Intent(this, UserActivity::class.java))
                            finish()
                        } else {                                                            //hesap pasif
                            auth.currentUser!!.delete().addOnCompleteListener {
                                loading.hideLoading()
                                val alert = AlertDialog.Builder(this)
                                alert.setTitle(currentMail)
                                alert.setMessage("Hesabınız Yöneticiler Tarafından Silinmiştir\n" + "Tekrar Giriş Yapamayacaksınız")
                                alert.setPositiveButton("Anladım") { dialogInterface, i ->
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                alert.setCancelable(false)
                                alert.show()
                            }
                        }
                    }
            }
        }

    }
}