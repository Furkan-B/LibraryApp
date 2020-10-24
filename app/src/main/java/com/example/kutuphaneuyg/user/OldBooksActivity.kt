package com.example.kutuphaneuyg.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_old_books.*

class OldBooksActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: OldBooksAdapter

    private var kitapList : ArrayList<BooksOfCurrnetUser> = ArrayList()
    private  var kitapDetails : ArrayList<MyBooksDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_books)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        getBooks()

        rvOldBooks.layoutManager = LinearLayoutManager(this)

        myAdapter = OldBooksAdapter(kitapList,this)
        rvOldBooks.adapter = myAdapter


    }

    private fun getBooks() {

        db.collection("Kullanıcılar").document(auth.currentUser!!.email.toString()).collection("aldigi_kitaplar")
            .whereEqualTo("teslim_etti_mi",true).get().addOnSuccessListener { querySnapshot ->
            if(querySnapshot != null){
                val document = querySnapshot.documents

                kitapDetails.clear()

                for(dc in document){

                    val myISBN = dc.id.toLong()
                    val alisTarihi = dc.get("alis_tarihi") as Timestamp
                    val teslimTarihi = dc.get("teslim_tarihi") as Timestamp

                    val details = MyBooksDetails(myISBN,alisTarihi,teslimTarihi)
                    kitapDetails.add(details)


                }
                db.collection("Kitaplar").get().addOnSuccessListener { querySnapshot ->
                    if(querySnapshot != null){
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
                                    val teslimTarihi = ktp.teslimTarihi

                                    val kitapClass = BooksOfCurrnetUser(isbn,kitapAdi,basimYili,sayfaSayisi,stok,yazar,yayinEvi,alisTarihi,teslimTarihi)

                                    kitapList.add(kitapClass)
                                    myAdapter.notifyDataSetChanged()
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

    }
}