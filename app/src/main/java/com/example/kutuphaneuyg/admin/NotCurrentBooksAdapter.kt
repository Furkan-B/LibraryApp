package com.example.kutuphaneuyg.admin

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kutuphaneuyg.R

class NotCurrentBooksAdapter(private val bookList : ArrayList<BookOfSomeOne>, private val myContext : Context) : RecyclerView.Adapter<NotCurrentBooksAdapter.BooksHolder>() {

    class BooksHolder(view: View) : RecyclerView.ViewHolder(view){
        var NotCurrentBooksAd : TextView?=null
        var NotCurrentBooksYazar : TextView?=null

        var NotCurrentBooksInfo : ImageView?=null
        var NotCurrentBooksUser : ImageView?= null

        init {
            NotCurrentBooksAd = view.findViewById(R.id.NotCurrentBooksAd)
            NotCurrentBooksYazar = view.findViewById(R.id.NotCurrentBooksYazar)
            NotCurrentBooksInfo = view.findViewById(R.id.NotCurrentBooksInfo)
            NotCurrentBooksUser = view.findViewById(R.id.NotCurrentBooksUser)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_not_current_books,parent,false)
        return BooksHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BooksHolder, position: Int) {

        holder.NotCurrentBooksAd?.text = bookList[position].kitapAdi
        holder.NotCurrentBooksYazar?.text = bookList[position].yazar


        holder.NotCurrentBooksInfo!!.setOnClickListener {

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

        holder.NotCurrentBooksUser!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle("Kitabın Bulunduğu Kişi")
            alert.setMessage("Adı : "+bookList[position].ad+
                    "\nSoyad : "+bookList[position].soyad+
                    "\nTelefon : "+bookList[position].telefon+
                    "\nMail Adresi : "+bookList[position].mail+
                    "\nAlış Tarihi : "+ bookList[position].alisTarihi.toDate())

            alert.setPositiveButton("Anladım"){dialogInterface, i ->
            }
            alert.show()

        }

    }

}