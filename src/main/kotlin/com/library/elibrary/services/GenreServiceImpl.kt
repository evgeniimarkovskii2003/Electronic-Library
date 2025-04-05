package com.library.elibrary.services

import com.library.elibrary.models.Genre
import com.library.elibrary.repositories.GenreRepository
import org.springframework.stereotype.Service

@Service
class GenreServiceImpl(private val genreRepository: GenreRepository) : GenreService {
    override fun create(genre: Genre): Genre {
        return genreRepository.create(genre)
    }

    override fun read(id: Int): Genre? {
        return genreRepository.read(id)
    }

    override fun update(genre: Genre, id: Int): Genre {
        return genreRepository.update(genre, id)
    }

    override fun delete(id: Int): Boolean {
        return genreRepository.delete(id)
    }

    override fun findAll(): List<Genre> {
        return genreRepository.findAll()
    }
}
