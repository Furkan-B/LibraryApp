package com.example.kutuphaneuyg.admin

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kutuphaneuyg.user.Books
import com.example.kutuphaneuyg.R
import com.google.firebase.firestore.FirebaseFirestore

class CurrentBooksAdapter(private val bookList : ArrayList<Books>, private val myContext : Context) : RecyclerView.Adapter<CurrentBooksAdapter.BooksHolder>() {

    private var db = FirebaseFirestore.getInstance()

    class BooksHolder(view: View) : RecyclerView.ViewHolder(view){
        var currentBooksAd : TextView?=null
        var currentBooksYazar : TextView?=null

        var currentBooksInfo : ImageView?=null
        var currentBooksDelete : ImageView?= null

        init {
            currentBooksAd = view.findViewById(R.id.currentBooksAd)
            currentBooksYazar = view.findViewById(R.id.currentBooksYazar)
            currentBooksInfo = view.findViewById(R.id.currentBooksInfo)
            currentBooksDelete = view.findViewById(R.id.currentBooksDelete)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_current_books,parent,false)
        return BooksHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BooksHolder, position: Int) {

        holder.currentBooksAd?.text = bookList[position].kitapAdi
        holder.currentBooksYazar?.text = bookList[position].yazar


        holder.currentBooksInfo!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle("Kitap Detayları")
            alert.setMessage("Kitap Adı : "+bookList[position].kitapAdi+
                    "\nYazarı : "+bookList[position].yazar+
                    "\nYayın Evi : "+bookList[position].yayinEvi+
                    "\nSayfa Sayısı : "+bookList[position].sayfaSayisi+
                    "\nBasım Yılı : "+ bookList[position].basimYili)

            alert.setPositiveButton("Anladım"){dialogInterface, i ->
            }
            alert.show()
        }

        holder.currentBooksDelete!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle(bookList[position].kitapAdi)
            alert.setMessage("Kitabı Sil")
            alert.setPositiveButton("Evet"){dialogInterface, i ->

                db.collection("Kitaplar").document(bookList[position].isbn.toString())
                    .delete()
                    .addOnSuccessListener { Toast.makeText(myContext,"Silme İşlemi Başarılı",Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { e -> Toast.makeText(myContext,"Silme İşlemi Başarısız ${e.localizedMessage}",Toast.LENGTH_SHORT).show() }
            }
            alert.setNegativeButton("Hayır"){dialogInterface, i ->
            }
            alert.show()

        }
    }

}