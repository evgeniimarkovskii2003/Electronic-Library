package com.library.elibrary.repositories

import com.library.elibrary.models.Journal
import com.library.elibrary.models.Reader

interface JournalRepository : CrudRepository<Journal> {
    fun findLastReaderByLibraryBookId(libraryBookId: Int): Reader?
}