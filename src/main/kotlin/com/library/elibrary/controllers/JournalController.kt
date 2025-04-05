package com.library.elibrary.controllers

import com.library.elibrary.dto.JournalDto
import com.library.elibrary.mappers.JournalMapper
import com.library.elibrary.models.Reader
import com.library.elibrary.services.JournalService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/journals")
class JournalController(
    private val journalService: JournalService,
    private val journalMapper: JournalMapper,
    @Value("\${elib.returnPeriod}")
    private val returnPeriod: Int
) {
    @PostMapping
    fun create(@RequestBody journalDto: JournalDto): ResponseEntity<JournalDto> {
        val createdJournal = journalService.create(journalMapper.toJournalEntity(journalDto))
        return ResponseEntity(journalMapper.toJournalDto(createdJournal), HttpStatus.CREATED)
    }
    @GetMapping("/return-period")
    fun getReturnPeriod(): ResponseEntity<Int> {
        return ResponseEntity.ok(returnPeriod)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): ResponseEntity<JournalDto> {
        val journal = journalService.read(id)
        return if (journal != null) {
            ResponseEntity(journalMapper.toJournalDto(journal), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody journalDto: JournalDto): ResponseEntity<JournalDto> {
        return try {
            val updatedJournal = journalService.update(journalMapper.toJournalEntity(journalDto), id)
            ResponseEntity(journalMapper.toJournalDto(updatedJournal), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): Boolean {
        return journalService.delete(id)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<JournalDto>> {
        val journals = journalService.findAll()
        val journalDtos = journals.map { journalMapper.toJournalDto(it) }
        return ResponseEntity(journalDtos, HttpStatus.OK)
    }
    @GetMapping("/last-reader/{libraryBookId}")
    fun getLastReaderForLibraryBook(@PathVariable libraryBookId: Int): ResponseEntity<Reader?> {
        val reader = journalService.findLastReaderByLibraryBookId(libraryBookId)
        return ResponseEntity.ok(reader)
    }
}
