package com.library.elibrary.repositories

import com.library.elibrary.models.Author
import com.library.elibrary.models.Book
import com.library.elibrary.models.Composition
import com.library.elibrary.models.Genre
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Date


@Component
class JdbcBookRepository(@Autowired private val jdbcTemplate: JdbcTemplate) : BookRepository {
    private fun insertBookCompositions(compositions: List<Composition>?, bookId: Int) {
        compositions?.forEach { composition ->
            jdbcTemplate.update(
                "INSERT INTO books_compositions (book_id, composition_id) VALUES(?, ?)",
                bookId, composition.id
            )
        }
    }

    @Transactional
    override fun create(entity: Book): Book {
        val sql = "INSERT INTO books (title, isbn, date_publication) VALUES (?, ?, ?) RETURNING id"
        val newId: Int = jdbcTemplate.queryForObject(
            sql,
            Int::class.java,
            entity.title,
            entity.isbn,
            Date.valueOf(entity.datePublication)
        )

        entity.compositions?.let { insertBookCompositions(it, newId) }

        return Book(newId, entity.compositions, entity.title, entity.isbn, entity.datePublication)
    }



    override fun delete(id: Int): Boolean {
        return jdbcTemplate.update("DELETE FROM books WHERE id = ?", id) > 0
    }

    override fun findAll(): List<Book> {
        return jdbcTemplate.query(
            "SELECT id, title, isbn, date_publication FROM books"
        ) { rs, _ ->
            val book = Book(
                rs.getInt("id"),
                null,
                rs.getString("title"),
                rs.getInt("isbn"),
                rs.getDate("date_publication").toLocalDate()
            )
            book
        }.toList()
    }

    override fun read(id: Int): Book? {
        val bookList = jdbcTemplate.queryForList(
            "SELECT b.id, b.title, b.isbn, b.date_publication FROM books b WHERE b.id = ?",
            id
        )
        val compositions = jdbcTemplate.query(
            """
        SELECT c.id, c.genre_id, g.title_genre AS genre_name, c.title
        FROM compositions c
        JOIN books_compositions bc ON c.id = bc.composition_id
        JOIN genres g ON c.genre_id = g.id
        WHERE bc.book_id = ?
        """,
            { rs, _ ->
                val compositionId = rs.getInt("id")
                val genre = Genre(rs.getInt("genre_id"), rs.getString("genre_name"))
                val authors = jdbcTemplate.query(
                    """
                SELECT a.id, a.full_name, a.pseudonym, a.birthdate
                FROM authors a
                JOIN authors_compositions ac ON a.id = ac.author_id
                WHERE ac.composition_id = ?
                """,
                    { rs, _ ->
                        Author(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("pseudonym"),
                            rs.getDate("birthdate")?.toLocalDate()
                        )
                    },
                    compositionId
                )
                Composition(
                    compositionId,
                    genre,
                    rs.getString("title"),
                    authors.toMutableList()
                )
            },
            id
        )
        return bookList.singleOrNull()?.let {
            Book(
                it["id"] as Int,
                compositions,
                it["title"] as String,
                it["isbn"] as Int,
                (it["date_publication"] as Date).toLocalDate()
            )
        }
    }

    @Transactional
    override fun update(entity: Book, id: Int): Book {
        jdbcTemplate.update(
            "UPDATE books SET title = ?, isbn = ?, date_publication = ? WHERE id = ?",
            entity.title, entity.isbn, entity.datePublication, id
        )

        jdbcTemplate.update("DELETE FROM books_compositions WHERE book_id = ?", id)

        insertBookCompositions(entity.compositions, id)
        return Book(id, entity.compositions, entity.title, entity.isbn, entity.datePublication)
    }
}