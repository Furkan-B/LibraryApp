package com.example.kutuphaneuyg.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_all_user.*


class AllUsersActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var myAdapter: AllUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        var a = getAllUsers()


        rvAllUser.layoutManager = LinearLayoutManager(this)

        myAdapter = AllUserAdapter(a,this)
        rvAllUser.adapter = myAdapter


    }

    fun getAllUsers() : ArrayList<Users>{

        var allUserList: ArrayList<Users> = ArrayList()

        db.collection("Kullanıcılar").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                else {
                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        var document = querySnapshot.documents

                        allUserList.clear()

                        for (dc in querySnapshot) {

                            val ad = dc.get("ad") as String
                            val soyad = dc.get("soyad") as String
                            val email = dc.get("email") as String
                            val telefon = dc.get("telefon") as String
                            var kayitTarihi = dc.get("kayit_tarih") as Timestamp

                            db.collection("Kullanıcılar").document(dc.id).collection("aldigi_kitaplar").whereEqualTo("teslim_etti_mi",false).get().addOnSuccessListener { querySnapshot ->
                                if (querySnapshot.isEmpty) {
                                    val user = Users(ad, soyad, email, telefon, kayitTarihi.toDate(), true)
                                    allUserList.add(user)
                                    myAdapter.notifyDataSetChanged()
                                } else {
                                    val user = Users(ad, soyad, email, telefon, kayitTarihi.toDate(), false)
                                    allUserList.add(user)
                                    myAdapter.notifyDataSetChanged()

                                }
                            }






                        }
                    }
                }

            }
        return allUserList
    }

}