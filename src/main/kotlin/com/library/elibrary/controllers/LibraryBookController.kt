package com.library.elibrary.controllers

import com.library.elibrary.dto.LibraryBookDto
import com.library.elibrary.mappers.LibraryBookMapper
import com.library.elibrary.services.LibraryBookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/librarybooks")
class LibraryBookController(
    private val libraryBookService: LibraryBookService,
    private val libraryBookMapper: LibraryBookMapper
) {

    @PostMapping
    fun create(@RequestBody libraryBookDto: LibraryBookDto): ResponseEntity<LibraryBookDto> {
        val createdLibraryBook = libraryBookService.create(libraryBookMapper.toLibraryBookEntity(libraryBookDto))
        return ResponseEntity(libraryBookMapper.toLibraryBookDto(createdLibraryBook), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<LibraryBookDto> {
        val libraryBook = libraryBookService.read(id)
        return if (libraryBook != null) {
            ResponseEntity(libraryBookMapper.toLibraryBookDto(libraryBook), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody libraryBookDto: LibraryBookDto): ResponseEntity<LibraryBookDto> {
        return try {
            val updatedLibraryBook = libraryBookService.update(libraryBookMapper.toLibraryBookEntity(libraryBookDto), id)
            ResponseEntity(libraryBookMapper.toLibraryBookDto(updatedLibraryBook), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): Boolean {
        return libraryBookService.delete(id)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<LibraryBookDto>> {
        val libraryBooks = libraryBookService.findAll()
        val libraryBookDtos = libraryBooks.map { libraryBookMapper.toLibraryBookDto(it) }
        return ResponseEntity(libraryBookDtos, HttpStatus.OK)
    }
}
