package com.library.elibrary.mappers

import com.library.elibrary.dto.CompositionDto
import com.library.elibrary.models.Composition
import org.springframework.stereotype.Component

@Component
class CompositionMapper(
    private val genreMapper: GenreMapper,
    private val authorMapper: AuthorMapper
) {
    fun toCompositionDto(composition: Composition): CompositionDto {
        return CompositionDto(
            id = composition.id,
            title = composition.title,
            genre = composition.genre?.let { genreMapper.toGenreDto(it) },
            authors = composition.authors?.map { authorMapper.toAuthorDto(it) }
        )
    }

    fun toCompositionEntity(compositionDto: CompositionDto): Composition {
        return Composition(
            id = compositionDto.id,
            title = compositionDto.title,
            genre = compositionDto.genre?.let { genreMapper.toGenreEntity(it) },
            authors = compositionDto.authors?.map { authorMapper.toAuthorEntity(it) }
        )
    }
}