package com.example.kutuphaneuyg.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.user.Books
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_not_current_books.*

class NotCurrentBooksActivity : AppCompatActivity() {


    private var kitapListMusteridekiler : ArrayList<Books> = ArrayList()
    private var musterininKitaplarıList : ArrayList<BookOfSomeOne> = ArrayList()


    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: NotCurrentBooksAdapter
    private var kitapList : ArrayList<Books> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_current_books)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        //getNotCurrentBooks()
        getNotCurrentBooks2()


//        rvNotCurrentBooks.layoutManager = LinearLayoutManager(this)
//
//        myAdapter = NotCurrentBooksAdapter(kitapList,this)
//        rvNotCurrentBooks.adapter = myAdapter

    }

    private fun getNotCurrentBooks2() {
        db.collection("Kitaplar").whereEqualTo("stok", false).get().addOnSuccessListener { querySnapshot ->
            if(querySnapshot != null){
                val document = querySnapshot.documents
                kitapListMusteridekiler.clear()
                for(dc in document){
                    val isbn = dc.get("ISBN_no") as Long
                    val basimYili = dc.get("basim_yili") as Long
                    val kitapAdi = dc.get("kitap") as String
                    val sayfaSayisi = dc.get("sayfa_sayisi") as Long
                    val yayinEvi = dc.get("yayin_evi") as String
                    val stok = dc.get("stok") as Boolean
                    val yazar = dc.get("yazar") as String

                    val kitapClass = Books(isbn, kitapAdi, basimYili, sayfaSayisi, stok, yazar, yayinEvi)
                    kitapListMusteridekiler.add(kitapClass)
                }

                db.collection("Kullanıcılar").get().addOnSuccessListener { querySnapshot ->
                    if(querySnapshot != null){
                        val document = querySnapshot.documents
                        for(user in document) {
                            db.collection("Kullanıcılar").document(user.id).collection("aldigi_kitaplar")
                                .whereEqualTo("teslim_etti_mi",false).get().addOnSuccessListener { querySnapshot ->
                                if(querySnapshot != null) {
                                    val document = querySnapshot.documents
                                    for (bookOfUser in document){
                                        for(kLM in kitapListMusteridekiler){
                                            if(kLM.isbn == bookOfUser.id.toLong()){
                                                val isbn = kLM.isbn
                                                val basimYili = kLM.basimYili
                                                val sayfaSayisi = kLM.sayfaSayisi
                                                val yayinEvi = kLM.yayinEvi
                                                val stok = kLM.stok
                                                val yazar = kLM.yazar
                                                val kitapAdi = kLM.kitapAdi
                                                val alisTarihi = bookOfUser.get("alis_tarihi") as Timestamp

                                                val ad = user.get("ad") as String
                                                val soyad = user.get("soyad") as String
                                                val mail = user.id
                                                val telefon = user.get("telefon") as String


                                                val mk =
                                                    BookOfSomeOne(
                                                        isbn,
                                                        kitapAdi,
                                                        basimYili,
                                                        sayfaSayisi,
                                                        stok,
                                                        yazar,
                                                        yayinEvi,
                                                        ad,
                                                        soyad,
                                                        mail,
                                                        telefon,
                                                        alisTarihi
                                                    )
                                                musterininKitaplarıList.add((mk))
                                                myAdapter.notifyDataSetChanged()


                                            }
                                        }
                                    }

                                }
                            }

                        }

                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
        }

        rvNotCurrentBooks.layoutManager = LinearLayoutManager(this)

        myAdapter = NotCurrentBooksAdapter(musterininKitaplarıList,this)
        rvNotCurrentBooks.adapter = myAdapter


    }

    fun getNotCurrentBooks() {
        db.collection("Kitaplar").whereEqualTo("stok", false).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            } else {
                if (querySnapshot != null) {
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents

                        kitapList.clear()

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

                            kitapList.add(kitapClass)
                            myAdapter.notifyDataSetChanged()

                        }
                    }
                }
            }
        }
    }

}