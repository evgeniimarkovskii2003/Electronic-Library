package com.library.elibrary.services

import com.library.elibrary.models.LibraryBook

interface LibraryBookService {
    fun create(libraryBook: LibraryBook): LibraryBook
    fun read(id: Int): LibraryBook?
    fun update(libraryBook: LibraryBook, id: Int): LibraryBook
    fun delete(id: Int): Boolean
    fun findAll(): List<LibraryBook>
}
