package com.library.elibrary.mappers

import com.library.elibrary.dto.ReaderDto
import com.library.elibrary.models.Reader
import org.springframework.stereotype.Component

@Component
class ReaderMapper {
    fun toReaderDto(reader: Reader): ReaderDto {
        return ReaderDto(
            id = reader.id,
            fullName = reader.fullName,
            email = reader.email
        )
    }

    fun toReaderEntity(readerDto: ReaderDto): Reader {
        return Reader(
            id = readerDto.id,
            fullName = readerDto.fullName,
            email = readerDto.email
        )
    }
}