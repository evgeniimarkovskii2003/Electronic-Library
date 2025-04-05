package com.library.elibrary.controllers

import com.library.elibrary.dto.GenreDto
import com.library.elibrary.mappers.GenreMapper
import com.library.elibrary.services.GenreService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/genres")
class GenreController(
    private val genreService: GenreService,
    private val genreMapper: GenreMapper
) {

    @PostMapping
    fun create(@RequestBody genreDto: GenreDto): ResponseEntity<GenreDto> {
        val createdGenre = genreService.create(genreMapper.toGenreEntity(genreDto))
        return ResponseEntity(genreMapper.toGenreDto(createdGenre), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<GenreDto> {
        val genre = genreService.read(id)
        return if (genre != null) {
            ResponseEntity(genreMapper.toGenreDto(genre), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody genreDto: GenreDto): ResponseEntity<GenreDto> {
        return try {
            val updatedGenre = genreService.update(genreMapper.toGenreEntity(genreDto), id)
            ResponseEntity(genreMapper.toGenreDto(updatedGenre), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): Boolean {
        return genreService.delete(id)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<GenreDto>> {
        val genres = genreService.findAll()
        val genreDtos = genres.map { genreMapper.toGenreDto(it) }
        return ResponseEntity(genreDtos, HttpStatus.OK)
    }
}
