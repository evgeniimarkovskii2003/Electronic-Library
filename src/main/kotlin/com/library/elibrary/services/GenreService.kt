package com.library.elibrary.services

import com.library.elibrary.models.Genre

interface GenreService {
    fun create(genre: Genre): Genre
    fun read(id: Int): Genre?
    fun update(genre: Genre, id: Int): Genre
    fun delete(id: Int): Boolean
    fun findAll(): List<Genre>
}
