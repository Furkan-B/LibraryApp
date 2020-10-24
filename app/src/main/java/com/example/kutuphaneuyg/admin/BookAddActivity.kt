package com.example.kutuphaneuyg.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kutuphaneuyg.LoadingScreen
import com.example.kutuphaneuyg.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_book_add.*

class BookAddActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var loading : LoadingScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_add)

        db = FirebaseFirestore.getInstance()
        loading = LoadingScreen(this)

    }

    fun saveBook(view : View) {
        val kitapAd = saveBookName.text.toString()
        val yazar = saveBookYazar.text.toString()
        val yayin = saveBookYayin.text.toString()
        val sayfa = saveBookSayfa.text.toString()
        val yil = saveBookYıl.text.toString()
        val isbn = saveBookISBN.text.toString()

        if(!kitapAd.isEmpty() && !yazar.isEmpty() && !yayin.isEmpty() && !sayfa.isEmpty() && !yil.isEmpty() && !isbn.isEmpty()){
            loading.showLoading()
            db.collection("Kitaplar").document(isbn).get().addOnSuccessListener { snapshot ->
                if(snapshot != null && snapshot.exists()){
                    loading.hideLoading()
                    saveBookISBN.setError("ISBN değiştir")
                }
                else{
                    val kitap = hashMapOf<String,Any>()
                    kitap.put("kitap",kitapAd)
                    kitap.put("yazar",yazar)
                    kitap.put("yayin_evi",yayin)
                    kitap.put("sayfa_sayisi",sayfa.toLong())
                    kitap.put("basim_yili",yil.toLong())
                    kitap.put("stok",true)
                    kitap.put("ISBN_no",isbn.toLong())

                    db.collection("Kitaplar").document(isbn.toString()).set(kitap).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            loading.hideLoading()
                            Toast.makeText(applicationContext,"Kitap Eklendi", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,AdminActivity::class.java))
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        loading.hideLoading()
                        Toast.makeText(applicationContext,exception.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else
            Toast.makeText(applicationContext,"Tüm Alanları Doldurun",Toast.LENGTH_SHORT).show()
    }
}