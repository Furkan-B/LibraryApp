package com.example.kutuphaneuyg.user

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BooksOfCurrentUserAdapter(private val bookList : ArrayList<BooksOfCurrnetUser>, private val myContext : Context) : RecyclerView.Adapter<BooksOfCurrentUserAdapter.BooksHolder>() {

    private var db = FirebaseFirestore.getInstance()
   // private val activity : BooksOfCurrentUserActivity = myContext as BooksOfCurrentUserActivity

    class BooksHolder(view: View) : RecyclerView.ViewHolder(view){
        var kitap : TextView?=null
        var alisTarihi : TextView?=null

        var info : ImageView?=null
        var iade : ImageView?= null

        init {
            kitap = view.findViewById(R.id.booksOfCurrentUserKitap)
            alisTarihi = view.findViewById(R.id.booksOfCurrentUserAlisTarih)
            info = view.findViewById(R.id.booksOfCurrentUserInfo)
            iade = view.findViewById(R.id.booksOfCurrentUserIade)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_books_of_current_user,parent,false)
        return BooksHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BooksHolder, position: Int) {

        holder.kitap?.text = bookList[position].kitapAdi
        holder.alisTarihi?.text = bookList[position].alisTarihi.toDate().toString()


        holder.info!!.setOnClickListener {

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

        holder.iade!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle(bookList[position].kitapAdi)
            alert.setMessage("Kitabını Teslim Etmek İstiyor Musun?")
            alert.setPositiveButton("Evet"){dialogInterface, i ->

                val currentMail = FirebaseAuth.getInstance().currentUser!!.email.toString()
                val currentISBN = bookList[position].isbn.toString()

                val updateBook = hashMapOf<String,Any>()
                updateBook.put("teslim_tarihi", Timestamp.now())
                updateBook.put("teslim_etti_mi",true)

                db.collection("Kullanıcılar").document(currentMail).collection("aldigi_kitaplar").document(currentISBN).update(updateBook).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        db.collection("Kitaplar").document(currentISBN).update("stok",true).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(myContext,"Kitap Teslim Edildi",Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(myContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(myContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }

            }
            alert.setNegativeButton("Hayır"){dialogInterface, i ->

            }
            alert.show()
        }

    }
}