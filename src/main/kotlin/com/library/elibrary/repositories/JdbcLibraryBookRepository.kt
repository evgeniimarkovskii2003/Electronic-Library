package com.library.elibrary.repositories

import com.library.elibrary.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component

@Component
class JdbcLibraryBookRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : LibraryBookRepository {
    private val libraryBookRowMapper = RowMapper<LibraryBook> { rs, _ ->
        LibraryBook(
            rs.getInt("id"),
            Book(
                rs.getInt("book_id"),
                mutableListOf(),
                rs.getString("title"),
                rs.getInt("isbn"),
                rs.getDate("date_publication").toLocalDate()
            ),
            rs.getString("status")
        )
    }

    override fun create(entity: LibraryBook): LibraryBook {
        val bookId = entity.book?.id ?: throw IllegalArgumentException("Book id is null")
        val sql = """
        INSERT INTO library_books (book_id, status) 
        VALUES (?, ?) RETURNING id
    """
        val newId = jdbcTemplate.queryForObject(sql, Int::class.java, bookId, entity.status)!!
        return LibraryBook(newId, entity.book, entity.status)
    }


    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM library_books WHERE id = ?", id) > 0
    }

    override fun findAll(): List<LibraryBook> {
        return jdbcTemplate.query(
            """
        SELECT library_books.id, library_books.status 
        FROM library_books
        """,
            RowMapper { rs, _ ->
                LibraryBook(
                    id = rs.getInt("id"),
                    book = null,
                    status = rs.getString("status")
                )
            }
        )
    }

    override fun read(id: Int): LibraryBook? {
        return jdbcTemplate.queryForObject(
            """
        SELECT library_books.id, books.id AS book_id, books.title, books.isbn, books.date_publication, library_books.status
        FROM library_books
        JOIN books ON library_books.book_id = books.id
        WHERE library_books.id = ?
        """,
            { rs, _ ->
                // Загружаем композиции для книги
                val compositions = jdbcTemplate.query(
                    """
                SELECT compositions.id, compositions.title, compositions.genre_id, genres.title_genre AS genre_name
                FROM compositions
                JOIN genres ON compositions.genre_id = genres.id
                JOIN books_compositions ON books_compositions.composition_id = compositions.id
                WHERE books_compositions.book_id = ?
                """,
                    { compRs, _ ->
                        // Загружаем авторов для каждой композиции
                        val authors = jdbcTemplate.query(
                            """
                        SELECT authors.id, authors.full_name, authors.pseudonym, authors.birthdate
                        FROM authors
                        JOIN authors_compositions ON authors_compositions.author_id = authors.id
                        WHERE authors_compositions.composition_id = ?
                        """,
                            { authorRs, _ ->
                                Author(
                                    id = authorRs.getInt("id"),
                                    fullName = authorRs.getString("full_name"),
                                    pseudonym = authorRs.getString("pseudonym"),
                                    birthdate = authorRs.getDate("birthdate")?.toLocalDate()
                                )
                            },
                            compRs.getInt("id") // ID композиции
                        )

                        // Возвращаем композицию с авторами
                        Composition(
                            id = compRs.getInt("id"),
                            title = compRs.getString("title"),
                            genre = Genre(
                                id = compRs.getInt("genre_id"),
                                title = compRs.getString("genre_name")
                            ),
                            authors = authors
                        )
                    },
                    rs.getInt("book_id") // ID книги
                )

                // Возвращаем объект LibraryBook с полной информацией
                LibraryBook(
                    id = rs.getInt("id"),
                    book = Book(
                        id = rs.getInt("book_id"),
                        title = rs.getString("title"),
                        isbn = rs.getInt("isbn"),
                        datePublication = rs.getDate("date_publication").toLocalDate(),
                        compositions = compositions
                    ),
                    status = rs.getString("status")
                )
            },
            id
        )
    }

    override fun update(entity: LibraryBook, id: Int): LibraryBook {
        jdbcTemplate.update(
            "UPDATE library_books SET book_id = ?, status = ? WHERE id = ?",
            entity.book?.id, entity.status, id
        )
        return LibraryBook(id, entity.book, entity.status)
    }
}