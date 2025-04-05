package com.library.elibrary.controllers

import com.library.elibrary.dto.ReaderDto
import com.library.elibrary.mappers.ReaderMapper
import com.library.elibrary.services.ReaderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/readers")
class ReaderController(
    private val readerService: ReaderService,
    private val readerMapper: ReaderMapper
) {

    @PostMapping
    fun create(@RequestBody readerDto: ReaderDto): ResponseEntity<ReaderDto> {
        val createdReader = readerService.create(readerMapper.toReaderEntity(readerDto))
        return ResponseEntity(readerMapper.toReaderDto(createdReader), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<ReaderDto> {
        val reader = readerService.read(id)
        return if (reader != null) {
            ResponseEntity(readerMapper.toReaderDto(reader), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody readerDto: ReaderDto): ResponseEntity<ReaderDto> {
        return try {
            val updatedReader = readerService.update(readerMapper.toReaderEntity(readerDto), id)
            ResponseEntity(readerMapper.toReaderDto(updatedReader), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): Boolean {
        return readerService.delete(id)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<ReaderDto>> {
        val readers = readerService.findAll()
        val readerDtos = readers.map { readerMapper.toReaderDto(it) }
        return ResponseEntity(readerDtos, HttpStatus.OK)
    }
}
