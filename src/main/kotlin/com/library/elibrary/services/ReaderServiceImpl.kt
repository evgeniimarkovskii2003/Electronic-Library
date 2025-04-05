package com.library.elibrary.services

import com.library.elibrary.models.Reader
import com.library.elibrary.repositories.ReaderRepository
import org.springframework.stereotype.Service

@Service
class ReaderServiceImpl(private val readerRepository: ReaderRepository) : ReaderService {
    override fun create(reader: Reader): Reader {
        return readerRepository.create(reader)
    }

    override fun read(id: Int): Reader? {
        return readerRepository.read(id)
    }

    override fun update(reader: Reader, id: Int): Reader {
        return readerRepository.update(reader, id)
    }

    override fun delete(id: Int): Boolean {
        return readerRepository.delete(id)
    }

    override fun findAll(): List<Reader> {
        return readerRepository.findAll()
    }
}
