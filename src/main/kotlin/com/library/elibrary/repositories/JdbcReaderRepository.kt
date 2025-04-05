package com.library.elibrary.repositories

import com.library.elibrary.models.Reader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Component
import java.sql.PreparedStatement


@Component
class JdbcReaderRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : ReaderRepository {
    private val readerRowMapper = RowMapper<Reader> { rs, _ ->
        Reader(
            rs.getInt("id"),
            rs.getString("full_name"),
            rs.getString("email")
        )
    }

    override fun create(entity: Reader): Reader {
        val sql = """
        INSERT INTO readers (full_name, email) 
        VALUES (?, ?) RETURNING id
    """
        val newId = jdbcTemplate.queryForObject(sql, Int::class.java, entity.fullName, entity.email)!!
        return Reader(newId, entity.fullName, entity.email)
    }


    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM readers WHERE id = ?", id) > 0
    }

    override fun findAll(): List<Reader> {
        return jdbcTemplate.query("SELECT id, full_name, email FROM readers", readerRowMapper)
    }

    override fun read(id: Int): Reader? {
        return jdbcTemplate.queryForObject(
            "SELECT id, full_name, email FROM readers WHERE id = ?", readerRowMapper, id
        )
    }

    override fun update(entity: Reader, id: Int): Reader {
        jdbcTemplate.update(
            "UPDATE readers SET full_name = ?, email = ? WHERE id = ?",
            entity.fullName, entity.email, id
        )
        return Reader(id, entity.fullName, entity.email)
    }
}