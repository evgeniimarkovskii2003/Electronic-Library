package com.library.elibrary.mappers

import com.library.elibrary.dto.JournalDto
import com.library.elibrary.models.Journal
import org.springframework.stereotype.Component

@Component
class JournalMapper(
    private val libraryBookMapper: LibraryBookMapper,
    private val readerMapper: ReaderMapper
) {
    fun toJournalDto(journal: Journal): JournalDto {
        return JournalDto(
            id = journal.id,
            dateOfIssue = journal.dateOfIssue,
            dateReturn = journal.dateReturn,
            libraryBook = journal.libraryBook?.let { libraryBookMapper.toLibraryBookDto(it) },
            reader = journal.reader?.let { readerMapper.toReaderDto(it) }
        )
    }

    fun toJournalEntity(journalDto: JournalDto): Journal {
        return Journal(
            id = journalDto.id,
            dateOfIssue = journalDto.dateOfIssue,
            dateReturn = journalDto.dateReturn,
            libraryBook = journalDto.libraryBook?.let { libraryBookMapper.toLibraryBookEntity(it) },
            reader = journalDto.reader?.let { readerMapper.toReaderEntity(it) }
        )
    }
}
