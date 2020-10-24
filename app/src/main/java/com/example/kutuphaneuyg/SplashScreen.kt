package com.example.kutuphaneuyg

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kutuphaneuyg.admin.AdminActivity
import com.example.kutuphaneuyg.user.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


      //  startActivity(Intent(this,AdminActivity::class.java))

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        splashImg.alpha = 0f
        splashImg.animate().setDuration(2000).alpha(1f).withEndAction {

            if(currentUser != null){
                whoAmI()
            }
            else goLoginScreen()
        }



    }

    private fun goLoginScreen() {

            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()

    }

    private fun whoAmI (){

        progressBar.alpha = 1f

        val currentMail = auth.currentUser!!.email.toString()

        db.collection("Yöneticiler").document(currentMail).get().addOnSuccessListener { snapshot ->
            if(snapshot != null && snapshot.exists()){  // yönetici
                //Toast.makeText(applicationContext,"Yöneticimiz: ${currentMail}",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
            else{                                                           // kullanıcı

                db.collection("Kullanıcılar").document(currentMail).get().addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) {    //hesap aktif
                        //Toast.makeText(applicationContext, "Welcome: ${currentMail}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, UserActivity::class.java))
                        finish()
                    } else {                                                                ////hesap pasif
                        auth.currentUser!!.delete().addOnCompleteListener {

                            val alert = AlertDialog.Builder(this)
                            alert.setTitle(currentMail)
                            alert.setMessage("Hesabınız Yöneticiler Tarafından Silinmiştir\n" + "Tekrar Giriş Yapamayacaksınız")
                            alert.setPositiveButton("Anladım"){dialogInterface, i ->
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