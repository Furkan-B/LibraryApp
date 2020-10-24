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
import com.example.kutuphaneuyg.R
import com.google.firebase.firestore.FirebaseFirestore

class AllUserAdapter(private val userList : ArrayList<Users>, private val myContext : Context) : RecyclerView.Adapter<AllUserAdapter.UsersHolder>() {

    private var db = FirebaseFirestore.getInstance()

    class UsersHolder(view: View) : RecyclerView.ViewHolder(view){
        var ad : TextView?=null
        var mail : TextView?=null

        var info : ImageView?=null
        var delete : ImageView?= null


        init {
            ad = view.findViewById(R.id.allUserAd)
            mail = view.findViewById(R.id.allUserMail)

            info = view.findViewById(R.id.allUserInfo)
            delete = view.findViewById(R.id.allUserDelete)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_all_user,parent,false)
        return UsersHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {

        val name = userList[position].ad +" "+ userList[position].soyad
        holder.ad?.text = name
        holder.mail?.text = userList[position].mail


        holder.info!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle("Kullanıcı Detayları")
            alert.setMessage(
                "Ad : " + userList[position].ad +
                        "\nSoyad : " + userList[position].soyad +
                        "\nE-Mail : " + userList[position].mail +
                        "\nTelefon : " + userList[position].telefon +
                        "\nKayit Tarihi : " + userList[position].kayitTarihi
            )

            alert.setPositiveButton("Anladım") { dialogInterface, i ->
            }
            alert.show()
        }

        holder.delete!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle(name)
            alert.setMessage("Kullanıcıyı Sil")
            alert.setPositiveButton("Evet"){dialogInterface, i ->

                if(userList[position].silinebilirMi){   //iade etmediği kitap yoksa silinebilir
                    db.collection("Kullanıcılar").document(userList[position].mail)
                        .delete()
                        .addOnSuccessListener { Toast.makeText(myContext,"Silme İşlemi Başarılı",Toast.LENGTH_SHORT).show() }
                        .addOnFailureListener { e -> Toast.makeText(myContext,"Silme İşlemi Başarısız ${e.localizedMessage}",Toast.LENGTH_SHORT).show() }

                }
                else{
                    Toast.makeText(myContext,"Kullanıcını Teslim Etmediği Kitap Olduğundan Silinemez",Toast.LENGTH_LONG).show()
                }
            }
            alert.setNegativeButton("Hayır"){dialogInterface, i ->
            }
            alert.show()
        }
    }

}