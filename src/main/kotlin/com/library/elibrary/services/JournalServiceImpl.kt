package com.library.elibrary.services

import com.library.elibrary.models.Journal
import com.library.elibrary.models.Reader
import com.library.elibrary.repositories.JournalRepository
import org.springframework.stereotype.Service

@Service
class JournalServiceImpl(private val journalRepository: JournalRepository) : JournalService {
    override fun create(journal: Journal): Journal {
        return journalRepository.create(journal)
    }

    override fun read(id: Int): Journal? {
        return journalRepository.read(id)
    }

    override fun update(journal: Journal, id: Int): Journal {
        return journalRepository.update(journal, id)
    }

    override fun delete(id: Int): Boolean {
        return journalRepository.delete(id)
    }

    override fun findAll(): List<Journal> {
        return journalRepository.findAll()
    }
    override fun findLastReaderByLibraryBookId(libraryBookId: Int): Reader?{
        return journalRepository.findLastReaderByLibraryBookId(libraryBookId)
    }
}
