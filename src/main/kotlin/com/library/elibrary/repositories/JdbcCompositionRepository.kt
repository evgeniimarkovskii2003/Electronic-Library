package com.library.elibrary.repositories

import com.library.elibrary.models.Author
import com.library.elibrary.models.Composition
import com.library.elibrary.models.Genre
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JdbcCompositionRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : CompositionRepository {

    private fun insertAuthorsCompositions(authors: List<Author>?, compositionId: Int) {
        authors?.forEach { author ->
            jdbcTemplate.update(
                "INSERT INTO authors_compositions (author_id, composition_id) VALUES(?,?)",
                author.id, compositionId
            )
        }
    }

    @Transactional
    override fun create(entity: Composition): Composition {
        val genreId = entity.genre?.id
        val sql = "INSERT INTO compositions (genre_id, title) VALUES (?, ?) RETURNING id"
        val newId: Int = jdbcTemplate.queryForObject(
            sql,
            Int::class.java,
            genreId,
            entity.title
        )

        entity.authors?.let { insertAuthorsCompositions(it, newId) }

        return Composition(newId, entity.genre, entity.title, entity.authors)
    }

    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM compositions WHERE id = ?", id) > 0
    }

    override fun findAll(): List<Composition> {
        return jdbcTemplate.query(
            "SELECT c.id, c.title, c.genre_id, g.id as genre_id, g.title_genre as genre_title " +
                    "FROM compositions c " +
                    "JOIN genres g ON c.genre_id = g.id"
        ) { rs, _ ->
            val composition = Composition(
                rs.getInt("id"),
                Genre(rs.getInt("genre_id"), rs.getString("genre_title")),
                rs.getString("title"),
                null
            )
            composition
        }.toList()
    }

    override fun read(id: Int): Composition? {
        val compositionList = jdbcTemplate.queryForList(
            "SELECT c.id, c.genre_id, c.title FROM compositions c WHERE c.id = ?",
            id
        )
        val authors = jdbcTemplate.query(
            " SELECT a.id, a.full_name, a.pseudonym, a.birthdate " +
                    "FROM authors a " +
                    "JOIN authors_compositions ac ON a.id = ac.author_id " +
                    "WHERE ac.composition_id = ?",
            { rs, _ ->
                Author(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("pseudonym"),
                    rs.getDate("birthdate")?.toLocalDate()
                )
            },
            id
        )
        return compositionList.singleOrNull()?.let {
            Composition(
                it["id"] as Int,
                Genre(it["genre_id"] as Int, ""),
                it["title"] as String,
                authors
            )
        }
    }

    @Transactional
    override fun update(entity: Composition, id: Int): Composition {
        jdbcTemplate.update(
            "UPDATE compositions SET genre_id = ?, title = ? WHERE id = ?",
            entity.genre?.id, entity.title, id
        )

        jdbcTemplate.update("DELETE FROM authors_compositions WHERE composition_id = ?", id)

        insertAuthorsCompositions(entity.authors, id)
        return Composition(id, entity.genre, entity.title, entity.authors)
    }
}



