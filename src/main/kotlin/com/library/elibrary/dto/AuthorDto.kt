package com.library.elibrary.dto

import java.time.LocalDate

data class AuthorDto (
    val id: Int? = null,
    val fullName: String? = null,
    val pseudonym: String? = null,
    val birthdate: LocalDate?
)