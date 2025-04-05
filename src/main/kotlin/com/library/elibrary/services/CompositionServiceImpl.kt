package com.library.elibrary.services

import com.library.elibrary.models.Composition
import com.library.elibrary.repositories.CompositionRepository
import org.springframework.stereotype.Service

@Service
class CompositionServiceImpl(private val compositionRepository: CompositionRepository) : CompositionService {
    override fun create(composition: Composition): Composition {
        return compositionRepository.create(composition)
    }

    override fun read(id: Int): Composition? {
        return compositionRepository.read(id)
    }

    override fun update(composition: Composition, id: Int): Composition {
        return compositionRepository.update(composition, id)
    }

    override fun delete(id: Int): Boolean {
        return compositionRepository.delete(id)
    }

    override fun findAll(): List<Composition> {
        return compositionRepository.findAll()
    }
}
