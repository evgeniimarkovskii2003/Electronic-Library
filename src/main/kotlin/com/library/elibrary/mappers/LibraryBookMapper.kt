package com.library.elibrary.mappers

import com.library.elibrary.dto.LibraryBookDto
import com.library.elibrary.models.LibraryBook
import org.springframework.stereotype.Component

@Component
class LibraryBookMapper(
    private val bookMapper: BookMapper
) {

    fun toLibraryBookDto(libraryBook: LibraryBook): LibraryBookDto {
        return LibraryBookDto(
            id = libraryBook.id,
            book = libraryBook.book?.let { bookMapper.toBookDto(it) },
            status = libraryBook.status
        )
    }

    fun toLibraryBookEntity(libraryBookDto: LibraryBookDto): LibraryBook {
        return LibraryBook(
            id = libraryBookDto.id,
            book = libraryBookDto.book?.let { bookMapper.toBookEntity(it) },
            status = libraryBookDto.status
        )
    }
}
