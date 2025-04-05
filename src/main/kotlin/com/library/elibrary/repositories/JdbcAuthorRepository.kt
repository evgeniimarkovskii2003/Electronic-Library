package com.library.elibrary.repositories

import com.library.elibrary.models.Author
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.Date

@Component
class JdbcAuthorRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : AuthorRepository {

    private val authorRowMapper = RowMapper<Author> { rs, _ ->
        Author(
            rs.getInt("id"),
            rs.getString("full_name"),
            rs.getString("pseudonym"),
            rs.getDate("birthdate").toLocalDate()
        )
    }

    override fun create(entity: Author): Author {
        val sql = "INSERT INTO authors (full_name, pseudonym, birthdate) VALUES (?, ?, ?) RETURNING id"
        val newId: Int = jdbcTemplate.queryForObject(
            sql,
            Int::class.java,
            entity.fullName,
            entity.pseudonym,
            Date.valueOf(entity.birthdate)
        )
        return Author(newId, entity.fullName, entity.pseudonym, entity.birthdate)
    }

    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM authors WHERE id = ?", id) > 0
    }

    override fun findAll(): List<Author> {
        return jdbcTemplate.query("SELECT id, full_name, pseudonym, birthdate FROM authors", authorRowMapper)
    }

    override fun read(id: Int): Author? {
        return jdbcTemplate.queryForObject(
            "SELECT id, full_name, pseudonym, birthdate FROM authors WHERE id = ?", authorRowMapper, id
        )
    }

    override fun update(entity: Author, id: Int): Author {
        jdbcTemplate.update(
            "UPDATE authors SET full_name = ?, pseudonym = ?, birthdate = ? WHERE id = ?",
            entity.fullName, entity.pseudonym, entity.birthdate, id
        )
        return Author(id, entity.fullName, entity.pseudonym, entity.birthdate)
    }
}