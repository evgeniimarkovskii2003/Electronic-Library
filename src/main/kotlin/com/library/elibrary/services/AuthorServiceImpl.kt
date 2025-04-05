package com.library.elibrary.services

import com.library.elibrary.models.Author
import com.library.elibrary.repositories.AuthorRepository
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : AuthorService {
    override fun create(author: Author): Author {
        return authorRepository.create(author)
    }

    override fun read(id: Int): Author? {
        return authorRepository.read(id)
    }

    override fun update(author: Author, id: Int): Author {
        return authorRepository.update(author, id)
    }

    override fun delete(id: Int): Boolean {
        return authorRepository.delete(id)
    }

    override fun findAll(): List<Author> {
        return authorRepository.findAll()
    }
}
