package com.example.kutuphaneuyg.user

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BooksAdapter(private val bookList : ArrayList<Books>, private val myContext : Context) : RecyclerView.Adapter<BooksAdapter.BooksHolder>() {

    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    class BooksHolder(view: View) : RecyclerView.ViewHolder(view){
        var textViewRV1 : TextView?=null
        var textViewRV2 : TextView?=null

        var buttonAddBook : ImageView?=null
        var buttonInfo : ImageView ?= null

        init {
            textViewRV1 = view.findViewById(R.id.textViewRV1)
            textViewRV2 = view.findViewById(R.id.textViewRV2)
            buttonAddBook = view.findViewById(R.id.buttonAddBook)
            buttonInfo = view.findViewById(R.id.buttonInfo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_books,parent,false)
        return BooksHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BooksHolder, position: Int) {

        holder.textViewRV1?.text = bookList[position].kitapAdi
        holder.textViewRV2?.text = bookList[position].yazar

        holder.buttonInfo!!.setOnClickListener {
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

        holder.buttonAddBook!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle(bookList[position].kitapAdi)
            alert.setMessage("Kitabını Almak İstiyor Musun?")
            alert.setPositiveButton("Evet"){dialogInterface, i ->

                val aldigiKitap = hashMapOf<String,Any>()
                aldigiKitap.put("ISBN",bookList[position].isbn)
                aldigiKitap.put("alis_tarihi", Timestamp.now())
                aldigiKitap.put("teslim_etti_mi",false)

                db.collection("Kullanıcılar").document(auth.currentUser!!.email.toString()).collection("aldigi_kitaplar").document(bookList[position].isbn.toString()).set(aldigiKitap).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        db.collection("Kitaplar").document(bookList[position].isbn.toString()).update("stok",false).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(myContext,"Kitap Alma İşlemi Başarılı",Toast.LENGTH_SHORT).show()
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