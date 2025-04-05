package com.library.elibrary.dto

data class CompositionDto(
    val id: Int? = null,
    val genre: GenreDto? = null,
    val title: String,
    val authors: List<AuthorDto>? = null
)