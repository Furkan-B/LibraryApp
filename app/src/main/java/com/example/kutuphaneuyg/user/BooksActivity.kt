package com.example.kutuphaneuyg.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutuphaneuyg.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_books.*
import java.util.*
import kotlin.collections.ArrayList

class BooksActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var myAdapter: BooksAdapter

    private var kitapListMevcut = ArrayList<Books>()
    private var displayList = ArrayList<Books>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        yazdir()

        rvBooks.layoutManager = LinearLayoutManager(this)

        myAdapter = BooksAdapter(displayList, this)
        rvBooks.adapter = myAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        val menuItem =menu!!.findItem(R.id.search)

        if(menuItem != null) {
            val searchView = menuItem.actionView as SearchView

            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Search..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if(newText!!.isNotEmpty()){
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())

                        kitapListMevcut.forEach {
                            if(it.kitapAdi.toLowerCase(Locale.getDefault()).contains(search))
                                displayList.add(it)
                        }
                        myAdapter.notifyDataSetChanged()
                    }
                    else{
                        displayList.clear()
                        displayList.addAll(kitapListMevcut)
                        myAdapter.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    fun yazdir(){
        db.collection("Kitaplar").whereEqualTo("stok", true).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Toast.makeText(applicationContext, firebaseFirestoreException.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            } else {
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    var document = querySnapshot.documents

                    displayList.clear()
                    kitapListMevcut.clear()

                    for(dc in document) {

                        val isbn = dc.get("ISBN_no") as Long
                        val basimYili = dc.get("basim_yili") as Long
                        val kitapAdi = dc.get("kitap") as String
                        val sayfaSayisi = dc.get("sayfa_sayisi") as Long
                        val yayinEvi = dc.get("yayin_evi") as String
                        val stok = dc.get("stok") as Boolean
                        val yazar = dc.get("yazar") as String

                        val kitapClass =
                            Books(
                                isbn,
                                kitapAdi,
                                basimYili,
                                sayfaSayisi,
                                stok,
                                yazar,
                                yayinEvi
                            )

                        displayList.add(kitapClass)
                        kitapListMevcut.add(kitapClass)
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}