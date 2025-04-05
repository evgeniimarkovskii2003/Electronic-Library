package com.library.elibrary.services

import com.library.elibrary.models.Composition

interface CompositionService {
    fun create(composition: Composition): Composition
    fun read(id: Int): Composition?
    fun update(composition: Composition, id: Int): Composition
    fun delete(id: Int): Boolean
    fun findAll(): List<Composition>
}