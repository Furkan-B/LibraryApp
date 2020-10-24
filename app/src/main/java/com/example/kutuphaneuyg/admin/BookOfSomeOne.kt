package com.example.kutuphaneuyg.admin

import com.example.kutuphaneuyg.user.Books
import com.google.firebase.Timestamp

class BookOfSomeOne(
    isbn: Long,
    kitapAdi: String,
    basimYili: Long,
    sayfaSayisi: Long,
    stok: Boolean,
    yazar: String,
    yayinEvi: String,
    var ad: String, var soyad: String, var mail: String, var telefon: String,var alisTarihi : Timestamp
) : Books(isbn, kitapAdi, basimYili, sayfaSayisi, stok, yazar, yayinEvi) {
}