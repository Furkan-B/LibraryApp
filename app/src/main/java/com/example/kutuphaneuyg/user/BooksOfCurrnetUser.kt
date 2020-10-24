package com.example.kutuphaneuyg.user

import com.google.firebase.Timestamp

class BooksOfCurrnetUser(
    isbn: Long,
    kitapAdi: String,
    basimYili: Long,
    sayfaSayisi: Long,
    stok: Boolean,
    yazar: String,
    yayinEvi: String,
    var alisTarihi: Timestamp,
    var teslimTarihi: Timestamp
) : Books(isbn, kitapAdi, basimYili, sayfaSayisi, stok, yazar, yayinEvi) {
}