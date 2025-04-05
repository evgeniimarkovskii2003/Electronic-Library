package com.library.elibrary.mappers

import com.library.elibrary.dto.GenreDto
import com.library.elibrary.models.Genre
import org.springframework.stereotype.Component

@Component
class GenreMapper {
    fun toGenreDto(genre: Genre): GenreDto {
        return GenreDto(
            id = genre.id,
            title = genre.title
        )
    }

    fun toGenreEntity(genreDto: GenreDto): Genre {
        return Genre(
            id = genreDto.id,
            title = genreDto.title
        )
    }
}