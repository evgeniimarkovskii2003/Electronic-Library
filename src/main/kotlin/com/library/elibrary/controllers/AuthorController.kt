package com.library.elibrary.controllers

import com.library.elibrary.dto.AuthorDto
import com.library.elibrary.services.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.library.elibrary.mappers.*

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService,
    private val authorMapper: AuthorMapper
) {

    @PostMapping
    fun create(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        val createdAuthor = authorService.create(authorMapper.toAuthorEntity(authorDto))
        return ResponseEntity(authorMapper.toAuthorDto(createdAuthor), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<AuthorDto> {
        val author = authorService.read(id)
        return if (author != null) {
            ResponseEntity(authorMapper.toAuthorDto(author), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        return try {
            val updatedAuthor = authorService.update(authorMapper.toAuthorEntity(authorDto), id)
            ResponseEntity(authorMapper.toAuthorDto(updatedAuthor), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Boolean> {
        return ResponseEntity(authorService.delete(id), HttpStatus.OK)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<AuthorDto>> {
        val authors = authorService.findAll()
        val authorDtos = authors.map { authorMapper.toAuthorDto(it) }
        return ResponseEntity(authorDtos, HttpStatus.OK)
    }
}

