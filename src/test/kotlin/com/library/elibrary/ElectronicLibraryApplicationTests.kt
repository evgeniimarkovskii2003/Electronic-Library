package com.library.elibrary


import com.library.elibrary.models.Author
import com.library.elibrary.models.Composition
import com.library.elibrary.models.Genre
import com.library.elibrary.repositories.JdbcAuthorRepository
import com.library.elibrary.repositories.JdbcCompositionRepository
import com.library.elibrary.repositories.JdbcGenreRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class ElectronicLibraryApplicationTests {

	@Autowired
	private lateinit var jdbcAuthorRepository: JdbcAuthorRepository
	@Autowired
	private lateinit var jdbcCompositionRepository: JdbcCompositionRepository
	@Autowired
	private lateinit var genreRepository: JdbcGenreRepository

	@Test
	fun testCreateGenre() {
		val genre = Genre(10, "кккк")
		val a = jdbcAuthorRepository.create(Author(
			fullName = "Test Author",
			birthdate = LocalDate.of(1990, 1, 1)
		))
		val newComposition =
			jdbcCompositionRepository.create(Composition(0, genre, "New Composition", mutableListOf((a))))
		val result = genreRepository.create(genre)
		assertTrue(result != null)
	}

}
