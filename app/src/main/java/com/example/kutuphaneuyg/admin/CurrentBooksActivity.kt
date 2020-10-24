package com.example.kutuphaneuyg.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.user.Books
import com.example.kutuphaneuyg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_current_books.*

class CurrentBooksActivity : AppCompatActivity() {



    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: CurrentBooksAdapter
    private var kitapListMevcut : ArrayList<Books> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_books)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        getCurrentBooks()


        rvCurrentBooks.layoutManager = LinearLayoutManager(this)

        myAdapter = CurrentBooksAdapter(kitapListMevcut,this)
        rvCurrentBooks.adapter = myAdapter



    }



    fun getCurrentBooks() {

        db.collection("Kitaplar").whereEqualTo("stok", true).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (querySnapshot != null) {
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents
                        kitapListMevcut.clear()

                        for (dc in document) {
                            val isbn = dc.get("ISBN_no") as Long
                            val basimYili = dc.get("basim_yili") as Long
                            val kitapAdi = dc.get("kitap") as String
                            val sayfaSayisi = dc.get("sayfa_sayisi") as Long
                            val yayinEvi = dc.get("yayin_evi") as String
                            val stok = dc.get("stok") as Boolean
                            val yazar = dc.get("yazar") as String

                            val kitapClass =
                                Books(
                                    isbn,
                                    kitapAdi,
                                    basimYili,
                                    sayfaSayisi,
                                    stok,
                                    yazar,
                                    yayinEvi
                                )

                            kitapListMevcut.add(kitapClass)
                            myAdapter.notifyDataSetChanged()

                        }
                    }
                }
            }
        }
    }

}