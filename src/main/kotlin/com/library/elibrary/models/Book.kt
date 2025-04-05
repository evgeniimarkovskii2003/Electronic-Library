package com.library.elibrary.models

import java.time.LocalDate

data class Book(
    val id: Int? = null,
    val compositions: List<Composition>? = null,
    val title: String,
    val isbn: Int,
    val datePublication: LocalDate
)