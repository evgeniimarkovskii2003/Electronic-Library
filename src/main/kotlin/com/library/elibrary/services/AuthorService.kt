package com.library.elibrary.services

import com.library.elibrary.models.Author

interface AuthorService {
    fun create(author: Author): Author
    fun read(id: Int): Author?
    fun update(author: Author, id: Int): Author
    fun delete(id: Int): Boolean
    fun findAll(): List<Author>
}
