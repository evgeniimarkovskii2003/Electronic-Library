package com.library.elibrary.dto

import java.time.LocalDate

data class BookDto(
    val id: Int? = null,
    val compositions: List<CompositionDto>? = null,
    val title: String,
    val isbn: Int,
    val datePublication: LocalDate
)