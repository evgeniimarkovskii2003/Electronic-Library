package com.library.elibrary.services

import com.library.elibrary.models.Book
import com.library.elibrary.repositories.BookRepository
import org.springframework.stereotype.Service

@Service
class BookServiceImpl(private val bookRepository: BookRepository) : BookService {
    override fun create(book: Book): Book {
        return bookRepository.create(book)
    }

    override fun read(id: Int): Book? {
        return bookRepository.read(id)
    }

    override fun update(book: Book, id: Int): Book {
        return bookRepository.update(book, id)
    }

    override fun delete(id: Int): Boolean {
        return bookRepository.delete(id)
    }

    override fun findAll(): List<Book> {
        return bookRepository.findAll()
    }
}
