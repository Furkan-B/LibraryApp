package com.example.kutuphaneuyg.user

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kutuphaneuyg.R
import com.google.firebase.firestore.FirebaseFirestore


class OldBooksAdapter(private val bookList : ArrayList<BooksOfCurrnetUser>, private val myContext : Context) : RecyclerView.Adapter<OldBooksAdapter.BooksHolder>() {

    private var db = FirebaseFirestore.getInstance()

    class BooksHolder(view: View) : RecyclerView.ViewHolder(view){
        var kitap : TextView?=null
        var info : ImageView?=null

        init {
            kitap = view.findViewById(R.id.managerListMail)
            info = view.findViewById(R.id.managerListInfo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_manager_list,parent,false)
        return BooksHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BooksHolder, position: Int) {

        holder.kitap?.text = bookList[position].kitapAdi

        holder.info!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle("Detaylar")
            alert.setMessage("Kitap Adı : "+bookList[position].kitapAdi+
                    "\nYazarı : "+bookList[position].yazar+
                    "\nYayın Evi : "+bookList[position].yayinEvi+
                    "\nSayfa Sayısı : "+bookList[position].sayfaSayisi+
                    "\nBasım Yılı : "+ bookList[position].basimYili+
                "\nAlınış Tarihi : "+ bookList[position].alisTarihi.toDate()+
                "\nTeslim Tarihi: "+ bookList[position].teslimTarihi.toDate())

            alert.setPositiveButton("Anladım"){dialogInterface, i ->
            }
            alert.show()
        }
    }
}