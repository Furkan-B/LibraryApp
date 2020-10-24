package com.example.kutuphaneuyg.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_books_of_current_user.*

class BooksOfCurrentUserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: BooksOfCurrentUserAdapter

    private var kitapList : ArrayList<BooksOfCurrnetUser> = ArrayList()
    private  var kitapDetails : ArrayList<MyBooksDetails> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_of_current_user)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        getBooks()

        rvBooksOfCurrentUser.layoutManager = LinearLayoutManager(this)

        myAdapter = BooksOfCurrentUserAdapter(kitapList,this)
        rvBooksOfCurrentUser.adapter = myAdapter


    }

    private fun getBooks() {

        db.collection("Kullanıcılar").document(auth.currentUser!!.email.toString()).collection("aldigi_kitaplar")
            .whereEqualTo("teslim_etti_mi",false).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            } else {
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val document1 = querySnapshot.documents

                    kitapDetails.clear()

                    for (dc in document1) {

                        val myISBN = dc.id.toLong()
                        val myDate = dc.get("alis_tarihi") as Timestamp

                        val details = MyBooksDetails(myISBN, myDate, myDate)
                        kitapDetails.add(details)
                    }
                    db.collection("Kitaplar").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if (firebaseFirestoreException != null) {
                            Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                        } else {
                            if (querySnapshot != null && !querySnapshot.isEmpty) {

                                val document = querySnapshot.documents

                                kitapList.clear()

                                for(ktp in kitapDetails){
                                    for(dc in document){
                                        if((dc.id.toLong()) == ktp.isbn){

                                            val isbn = dc.get("ISBN_no") as Long
                                            val basimYili = dc.get("basim_yili") as Long
                                            val kitapAdi = dc.get("kitap") as String
                                            val sayfaSayisi = dc.get("sayfa_sayisi") as Long
                                            val yayinEvi = dc.get("yayin_evi") as String
                                            val stok = dc.get("stok") as Boolean
                                            val yazar = dc.get("yazar") as String
                                            val alisTarihi = ktp.alisTarihi

                                            val kitapClass = BooksOfCurrnetUser(isbn,kitapAdi,basimYili,sayfaSayisi,stok,yazar,yayinEvi,alisTarihi,alisTarihi)

                                            kitapList.add(kitapClass)
                                            myAdapter.notifyDataSetChanged()
                                        }
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
    }

}