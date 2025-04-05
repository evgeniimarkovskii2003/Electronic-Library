package com.library.elibrary.models

import java.time.LocalDate

data class Journal(
    val id: Int? = null,
    val dateOfIssue: LocalDate,
    val dateReturn: LocalDate,
    val libraryBook: LibraryBook? = null,
    val reader: Reader? = null
)