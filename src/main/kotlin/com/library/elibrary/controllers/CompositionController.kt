package com.library.elibrary.controllers

import com.library.elibrary.dto.CompositionDto
import com.library.elibrary.mappers.CompositionMapper
import com.library.elibrary.services.CompositionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/compositions")
class CompositionController(
    private val compositionService: CompositionService,
    private val compositionMapper: CompositionMapper
) {

    @PostMapping
    fun create(@RequestBody compositionDto: CompositionDto): ResponseEntity<CompositionDto> {
        val createdComposition = compositionService.create(compositionMapper.toCompositionEntity(compositionDto))
        return ResponseEntity(compositionMapper.toCompositionDto(createdComposition), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<CompositionDto> {
        val composition = compositionService.read(id)
        return if (composition != null) {
            ResponseEntity(compositionMapper.toCompositionDto(composition), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody compositionDto: CompositionDto): ResponseEntity<CompositionDto> {
        return try {
            val updatedComposition = compositionService.update(compositionMapper.toCompositionEntity(compositionDto), id)
            ResponseEntity(compositionMapper.toCompositionDto(updatedComposition), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): Boolean {
        return compositionService.delete(id)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<CompositionDto>> {
        val compositions = compositionService.findAll()
        val compositionDtos = compositions.map { compositionMapper.toCompositionDto(it) }
        return ResponseEntity(compositionDtos, HttpStatus.OK)
    }
}
