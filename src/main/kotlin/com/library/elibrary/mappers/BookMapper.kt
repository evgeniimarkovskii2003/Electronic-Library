package com.library.elibrary.mappers

import com.library.elibrary.dto.*
import com.library.elibrary.models.*
import org.springframework.stereotype.Component

@Component
class BookMapper(private val compositionMapper: CompositionMapper) {

    fun toBookDto(book: Book): BookDto {
        return BookDto(
            id = book.id,
            title = book.title,
            isbn = book.isbn,
            datePublication = book.datePublication,
            compositions = book.compositions?.map { compositionMapper.toCompositionDto(it) }
        )
    }

    fun toBookEntity(bookDto: BookDto): Book {
        return Book(
            id = bookDto.id,
            title = bookDto.title,
            isbn = bookDto.isbn,
            datePublication = bookDto.datePublication,
            compositions = bookDto.compositions?.map { compositionMapper.toCompositionEntity(it) }
        )
    }

}
