package com.library.elibrary.repositories

import com.library.elibrary.models.Genre
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import java.sql.PreparedStatement

@Component
class JdbcGenreRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : GenreRepository {
    private val genreRowMapper = RowMapper<Genre> { rs, _ ->
        Genre(
            rs.getInt("id"),
            rs.getString("title_genre")
        )
    }

    override fun create(entity: Genre): Genre {
        val sql = "INSERT INTO genres (title_genre) VALUES (?) RETURNING id"
        val newId: Int = jdbcTemplate.queryForObject(
            sql,
            Int::class.java,
            entity.title
        )

        return Genre(newId, entity.title)
    }

    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM genres WHERE id = ?", id) > 0
    }

    override fun findAll(): List<Genre> {
        return jdbcTemplate.query("SELECT id, title_genre FROM genres", genreRowMapper)
    }

    override fun read(id: Int): Genre? {
        return jdbcTemplate.queryForObject(
            "SELECT id, title_genre FROM genres WHERE id = ?", genreRowMapper, id
        )
    }

    override fun update(entity: Genre, id: Int): Genre {
        jdbcTemplate.update(
            "UPDATE genres SET title_genre = ? WHERE id = ?",
            entity.title, id
        )
        return Genre(id, entity.title)
    }
}