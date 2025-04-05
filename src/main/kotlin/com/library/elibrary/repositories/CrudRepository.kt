package com.library.elibrary.repositories

interface CrudRepository<T>{
    fun create(entity: T): T
    fun findAll(): List<T>
    fun read(id: Int): T?
    fun update(entity: T, id: Int): T
    fun delete(id: Int): Boolean
}