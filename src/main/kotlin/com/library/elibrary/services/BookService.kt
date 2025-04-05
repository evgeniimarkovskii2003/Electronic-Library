package com.library.elibrary.services

import com.library.elibrary.models.Book

interface BookService {
    fun create(book: Book): Book
    fun read(id: Int): Book?
    fun update(book: Book, id: Int): Book
    fun delete(id: Int): Boolean
    fun findAll(): List<Book>
}
