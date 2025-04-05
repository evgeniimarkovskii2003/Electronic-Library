package com.library.elibrary.services

import com.library.elibrary.models.Reader

interface ReaderService {
    fun create(reader: Reader): Reader
    fun read(id: Int): Reader?
    fun update(reader: Reader, id: Int): Reader
    fun delete(id: Int): Boolean
    fun findAll(): List<Reader>
}