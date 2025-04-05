package com.library.elibrary.services

import com.library.elibrary.models.Journal
import com.library.elibrary.models.Reader

interface JournalService {
    fun create(journal: Journal): Journal
    fun read(id: Int): Journal?
    fun update(journal: Journal, id: Int): Journal
    fun delete(id: Int): Boolean
    fun findAll(): List<Journal>
    fun findLastReaderByLibraryBookId(libraryBookId: Int): Reader?
}
