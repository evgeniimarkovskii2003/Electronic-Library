package com.library.elibrary.models

data class Composition(
    val id: Int? = null,
    val genre: Genre? = null,
    val title: String,
    val authors: List<Author>? = null
)