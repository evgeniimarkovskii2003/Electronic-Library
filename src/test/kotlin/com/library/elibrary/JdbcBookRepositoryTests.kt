import com.library.elibrary.ElectronicLibraryApplication
import com.library.elibrary.models.Author
import com.library.elibrary.models.Book
import com.library.elibrary.models.Composition
import com.library.elibrary.models.Genre
import com.library.elibrary.repositories.JdbcAuthorRepository
import com.library.elibrary.repositories.JdbcBookRepository
import com.library.elibrary.repositories.JdbcCompositionRepository
import com.library.elibrary.repositories.JdbcGenreRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(classes = [ElectronicLibraryApplication::class])
class JdbcBookRepositoryTest {

    @Autowired
    private lateinit var jdbcBookRepository: JdbcBookRepository

    @Autowired
    private lateinit var jdbcCompositionRepository: JdbcCompositionRepository

    @Autowired
    private lateinit var jdbcGenreRepository: JdbcGenreRepository

    @Autowired
    private lateinit var jdbcAuthorRepository: JdbcAuthorRepository

    private lateinit var testGenre: Genre
    private lateinit var testComposition: Composition
    private lateinit var testAuthor: Author
    private lateinit var testBook: Book

    @BeforeEach
    fun setUp() {
        testGenre = jdbcGenreRepository.create(Genre(
            title = "Test Genre"
        ))
        testAuthor = jdbcAuthorRepository.create(Author(
            fullName = "Test Author",
            birthdate = LocalDate.of(1990, 1, 1)
        ))
        testComposition = jdbcCompositionRepository.create(
            Composition(
                genre = testGenre,
                title = "Test Composition",
                authors = listOf(testAuthor)
            )
        )
        testBook = jdbcBookRepository.create(
            Book(
                compositions = listOf(testComposition),
                title = "Test Book",
                isbn = 123456789,
                datePublication = LocalDate.now()
            )
        )
    }

    @Test
    fun testCreateBook() {
        val newComposition =
            jdbcCompositionRepository.create(Composition(0, testGenre, "New Composition", mutableListOf(testAuthor)))

        val created =
            jdbcBookRepository.create(Book(0, mutableListOf(newComposition), "New Book", 987654321, LocalDate.now()))
        assertNotNull(created.id)
        assertTrue { created.id!! > 0 }
        assertEquals("New Book", created.title)
        assertEquals(987654321, created.isbn)
        assertEquals(newComposition, created.compositions?.first())
    }

    @Test
    fun testReadBook() {
        val readBook = jdbcBookRepository.read(testBook.id!!)
        assertNotNull(readBook)
        assertEquals(testBook.id, readBook.id)
        assertEquals(testBook.title, readBook.title)
        assertEquals(testBook.isbn, readBook.isbn)
        assertEquals(testBook.datePublication, readBook.datePublication)
        assertTrue { readBook.compositions!!.isNotEmpty() }
        assertEquals(testComposition.id, readBook.compositions!![0].id)
    }

    @Test
    fun testUpdateBook() {
        val updatedBook =
            Book(testBook.id, mutableListOf(testComposition), "Updated Test Book", 123456789, LocalDate.now())
        jdbcBookRepository.update(updatedBook, testBook.id!!)
        val readBook = jdbcBookRepository.read(testBook.id!!)
        assertNotNull(readBook)
        assertEquals(updatedBook.title, readBook.title)
        assertEquals(updatedBook.isbn, readBook.isbn)
        assertEquals(updatedBook.datePublication, readBook.datePublication)
        assertEquals(updatedBook.compositions?.size, readBook.compositions?.size)
    }

    @Test
    fun testFindAll() {
        val books = jdbcBookRepository.findAll()
        assertTrue { books.isNotEmpty() }
    }

    @Test
    fun testCreateBookWithCompositions() {
        val newComposition =
            jdbcCompositionRepository.create(Composition(0, testGenre, "New Composition", mutableListOf()))

        val bookWithComposition =
            Book(0, mutableListOf(testComposition, newComposition), "Book With Composition", 123456789, LocalDate.now())
        val created = jdbcBookRepository.create(bookWithComposition)
        assertNotNull(created.id)
        assertTrue { created.id!! > 0 }
        val readBook = jdbcBookRepository.read(created.id!!)
        assertNotNull(readBook)
        assertTrue { readBook.compositions?.size == 2 }
        assertTrue { readBook.compositions?.any { it.id == testComposition.id } == true }
        assertTrue { readBook.compositions?.any { it.id == newComposition.id } == true }
    }

    @Test
    fun testUpdateBookWithCompositions() {

        val newComposition =
            jdbcCompositionRepository.create(Composition(0, testGenre, "New Composition", mutableListOf()))

        val updatedBook = Book(
            testBook.id,
            mutableListOf(testComposition, newComposition),
            "Updated Test Book",
            123456789,
            LocalDate.now()
        )
        jdbcBookRepository.update(updatedBook, testBook.id!!)
        val readBook = jdbcBookRepository.read(testBook.id!!)
        assertNotNull(readBook)
        assertTrue { readBook.compositions!!.size == updatedBook.compositions!!.size }
        assertTrue { readBook.compositions!!.any { it.id == testComposition.id } }
        assertTrue { readBook.compositions!!.any { it.id == newComposition.id } }
    }

    @Test
    fun testDeleteBook() {
        val deleted = jdbcBookRepository.delete(testBook.id!!)
        assertTrue { deleted }
        val readBook = jdbcBookRepository.read(testBook.id!!)
        assertNull(readBook)
    }
}