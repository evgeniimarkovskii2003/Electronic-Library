package com.library.elibrary.services

import com.library.elibrary.models.LibraryBook
import com.library.elibrary.repositories.LibraryBookRepository
import org.springframework.stereotype.Service

@Service
class LibraryBookServiceImpl(private val libraryBookRepository: LibraryBookRepository) : LibraryBookService {

    override fun create(libraryBook: LibraryBook): LibraryBook {
        return libraryBookRepository.create(libraryBook)
    }

    override fun read(id: Int): LibraryBook? {
        return libraryBookRepository.read(id)
    }

    override fun update(libraryBook: LibraryBook, id: Int): LibraryBook {
        return libraryBookRepository.update(libraryBook, id)
    }

    override fun delete(id: Int): Boolean {
        return libraryBookRepository.delete(id)
    }

    override fun findAll(): List<LibraryBook> {
        return libraryBookRepository.findAll()
    }
}
