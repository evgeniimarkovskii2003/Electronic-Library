package com.library.elibrary.mappers

import com.library.elibrary.dto.AuthorDto
import com.library.elibrary.models.Author
import org.springframework.stereotype.Component

@Component
class AuthorMapper {
    fun toAuthorDto(author: Author): AuthorDto {
        return AuthorDto(
            id = author.id,
            fullName = author.fullName,
            pseudonym = author.pseudonym,
            birthdate = author.birthdate
        )
    }

    fun toAuthorEntity(authorDto: AuthorDto): Author {
        return Author(
            id = authorDto.id,
            fullName = authorDto.fullName,
            pseudonym = authorDto.pseudonym,
            birthdate = authorDto.birthdate
        )
    }
}