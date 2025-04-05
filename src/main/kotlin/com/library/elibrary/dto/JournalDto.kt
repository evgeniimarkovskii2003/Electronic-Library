package com.library.elibrary.dto

import java.time.LocalDate

data class JournalDto(
    val id: Int? = null,
    val dateOfIssue: LocalDate,
    val dateReturn: LocalDate,
    val libraryBook: LibraryBookDto? = null,
    val reader: ReaderDto? = null
)