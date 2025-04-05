package com.library.elibrary.repositories

import com.library.elibrary.models.Book
import com.library.elibrary.models.Journal
import com.library.elibrary.models.LibraryBook
import com.library.elibrary.models.Reader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder

import org.springframework.stereotype.Component
import java.sql.Date
import java.sql.PreparedStatement

@Component
class JdbcJournalRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : JournalRepository {
    private val journalRowMapper = RowMapper<Journal> { rs, _ ->
        Journal(
            rs.getInt("id"),
            rs.getDate("date_of_issue").toLocalDate(),
            rs.getDate("date_return").toLocalDate(),
            LibraryBook(
                rs.getInt("library_book_id"),
                Book(
                    rs.getInt("id"), mutableListOf(), rs.getString("title"),
                    rs.getInt("isbn"), rs.getDate("date_publication").toLocalDate()
                ),
                rs.getString("status")
            ),
            Reader(
                rs.getInt("reader_id"),
                rs.getString("full_name"),
                rs.getString("email")
            )
        )
    }
    private val journalRowMapper2 = RowMapper<Journal> { rs, _ ->
        Journal(
            rs.getInt("id"),
            rs.getDate("date_of_issue").toLocalDate(),
            rs.getDate("date_return").toLocalDate(),
            LibraryBook(
                rs.getInt("library_book_id"),
                null,
                rs.getString("status")
            ),
            Reader(
                rs.getInt("reader_id"),
                rs.getString("full_name"),
                rs.getString("email")
            )
        )
    }

    override fun create(entity: Journal): Journal {
        val sql = """
        INSERT INTO journals (date_of_issue, date_return, library_book_id, reader_id) 
        VALUES (?, ?, ?, ?) RETURNING id
    """
        val newId = jdbcTemplate.queryForObject(
            sql,
            Int::class.java,
            entity.dateOfIssue,
            entity.dateReturn,
            entity.libraryBook?.id,
            entity.reader?.id
        )
        return Journal(newId, entity.dateOfIssue, entity.dateReturn, entity.libraryBook, entity.reader)
    }

    override fun findAll(): List<Journal> {
        return jdbcTemplate.query(
            """
        SELECT journals.id, date_of_issue, date_return 
        FROM journals
        """,
            RowMapper { rs, _ ->
                Journal(
                    id = rs.getInt("id"),
                    dateOfIssue = rs.getDate("date_of_issue").toLocalDate(),
                    dateReturn = rs.getDate("date_return").toLocalDate(),
                    libraryBook = null,
                    reader = null
                )
            }
        )
    }

    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM journals WHERE id = ?", id) > 0
    }

    override fun read(id: Int): Journal? {
        return jdbcTemplate.queryForObject(
            """
        SELECT journals.id, date_of_issue, date_return, library_book_id, readers.id AS reader_id, full_name, email, 
               books.id AS book_id, title, isbn, date_publication, library_books.status
        FROM journals
        JOIN readers ON journals.reader_id = readers.id
        JOIN library_books ON journals.library_book_id = library_books.id
        JOIN books ON library_books.book_id = books.id
        WHERE journals.id = ?
        """,
            RowMapper { rs, _ ->
                Journal(
                    id = rs.getInt("id"),
                    dateOfIssue = rs.getDate("date_of_issue").toLocalDate(),
                    dateReturn = rs.getDate("date_return").toLocalDate(),
                    libraryBook = LibraryBook(
                        id = rs.getInt("library_book_id"),
                        book = Book(
                            id = rs.getInt("book_id"),
                            title = rs.getString("title"),
                            isbn = rs.getInt("isbn"),
                            datePublication = rs.getDate("date_publication").toLocalDate()
                        ),
                        status = rs.getString("status")
                    ),
                    reader = Reader(
                        id = rs.getInt("reader_id"),
                        fullName = rs.getString("full_name"),
                        email = rs.getString("email")
                    )
                )
            },
            id
        )
    }

    override fun update(entity: Journal, id: Int): Journal {
        jdbcTemplate.update(
            "UPDATE journals SET date_of_issue = ?, date_return = ?, " +
                    "library_book_id = ?, reader_id = ? WHERE id = ?",
            Date.valueOf(entity.dateOfIssue),
            Date.valueOf(entity.dateReturn), entity.libraryBook?.id, entity.reader?.id, id
        )
        return Journal(id, entity.dateOfIssue, entity.dateReturn, entity.libraryBook, entity.reader)
    }
    override fun findLastReaderByLibraryBookId(libraryBookId: Int): Reader? {
        return jdbcTemplate.query("""
        SELECT r.id, r.full_name, r.email
        FROM journals j
        JOIN readers r ON j.reader_id = r.id
        WHERE j.library_book_id = ?
        ORDER BY j.id DESC
        LIMIT 1
    """, RowMapper { rs, _ ->
            Reader(
                id = rs.getInt("id"),
                fullName = rs.getString("full_name"),
                email = rs.getString("email")
            )
        }, libraryBookId).firstOrNull()
    }
}