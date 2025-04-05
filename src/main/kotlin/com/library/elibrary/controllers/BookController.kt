package com.library.elibrary.controllers

import com.library.elibrary.dto.BookDto
import com.library.elibrary.mappers.BookMapper
import com.library.elibrary.services.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService,
    private val bookMapper: BookMapper
) {

    @PostMapping
    fun create(@RequestBody bookDto: BookDto): ResponseEntity<BookDto> {
        val createdBook = bookService.create(bookMapper.toBookEntity(bookDto))
        return ResponseEntity(bookMapper.toBookDto(createdBook), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<BookDto> {
        val book = bookService.read(id)
        return if (book != null) {
            ResponseEntity(bookMapper.toBookDto(book), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody bookDto: BookDto): ResponseEntity<BookDto> {
        return try {
            val updatedBook = bookService.update(bookMapper.toBookEntity(bookDto), id)
            ResponseEntity(bookMapper.toBookDto(updatedBook), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): Boolean {
        return bookService.delete(id)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<BookDto>> {
        val books = bookService.findAll()
        val bookDtos = books.map { bookMapper.toBookDto(it) }
        return ResponseEntity(bookDtos, HttpStatus.OK)
    }
}
