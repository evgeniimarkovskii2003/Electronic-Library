package com.library.elibrary.models
import java.time.LocalDate

data class Author(
    val id: Int? = null,
    val fullName: String? = null,
    val pseudonym: String? = null,
    val birthdate: LocalDate?
)
