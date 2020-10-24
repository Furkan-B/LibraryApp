package com.example.kutuphaneuyg.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manager_list.*

class ManagerListActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var myManagerList : ArrayList<Manager> = ArrayList()
    private lateinit var myAdapter: ManagerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_list)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        getAllManager()

        rvManagerList.layoutManager = LinearLayoutManager(this)
        myAdapter = ManagerListAdapter(myManagerList,this)
        rvManagerList.adapter = myAdapter

    }

    private fun getAllManager() {

        db.collection("Yöneticiler").get().addOnSuccessListener { querySnapshot ->
            if(querySnapshot != null){
                myManagerList.clear()
                for (dc in querySnapshot) {
                    val sicilNo = dc.get("sicil_no") as Long
                    val email = dc.get("email") as String
                    val kayitTarihi = dc.get("kayit_tarih") as Timestamp

                    val managerClass = Manager(sicilNo.toLong(),email,kayitTarihi)
                    myManagerList.add(managerClass)
                    myAdapter.notifyDataSetChanged()


                }
            }
        }
    }
}