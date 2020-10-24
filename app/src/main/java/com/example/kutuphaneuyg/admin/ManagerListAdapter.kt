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

class ManagerListAdapter (private val managersList : ArrayList<Manager>, private val myContext : Context) : RecyclerView.Adapter<ManagerListAdapter.ManagerHolder>() {


    class ManagerHolder(view: View) : RecyclerView.ViewHolder(view){
        var mail : TextView?=null
        var info : ImageView?=null

        init {
            mail = view.findViewById(R.id.managerListMail)
            info = view.findViewById(R.id.managerListInfo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerHolder {//Adapter oluştuğunda Holder’ı başlatır
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_row_manager_list,parent,false)
        return ManagerHolder(view)
    }

    override fun getItemCount(): Int { //Liste eleman sayısını döndürür.
        return managersList.size
    }

    override fun onBindViewHolder(holder: ManagerHolder, position: Int) { //onCreateViewHolder() metodundan dönen veriyi bağlar.

        holder.mail?.text = managersList[position].mail

        holder.info!!.setOnClickListener {

            val alert = AlertDialog.Builder(myContext)
            alert.setTitle("Kullanıcı Detayları")
            alert.setMessage(
                "Sicil No : " + managersList[position].sicilNo +
                        "\nE-Mail : " + managersList[position].mail +
                        "\nKayit Tarihi : " + managersList[position].kayitTarihi.toDate()
            )

            alert.setPositiveButton("Anladım") { dialogInterface, i ->
            }
            alert.show()
        }

    }

}