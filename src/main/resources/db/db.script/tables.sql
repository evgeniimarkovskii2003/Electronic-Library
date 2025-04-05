CREATE TABLE authors (
	id SERIAL PRIMARY KEY,
	full_name VARCHAR(300),
	pseudonym VARCHAR(100),
	birthdate DATE
);
CREATE TABLE genres(
	id SERIAL PRIMARY KEY,
	title_genre VARCHAR(100) NOT NULL
);
CREATE TABLE compositions(
	id SERIAL PRIMARY KEY,
	genre_id INT,
	FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE,
	title VARCHAR(300) NOT NULL
);
CREATE TABLE authors_compositions(
	id SERIAL PRIMARY KEY,
	author_id INT,
	composition_id INT,
	FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE,
	FOREIGN KEY (composition_id) REFERENCES compositions(id) ON DELETE CASCADE,
	UNIQUE(author_id, composition_id)
);
CREATE TABLE readers(
	id SERIAL PRIMARY KEY,
	full_name VARCHAR(300) NOT NULL,
	email VARCHAR (100) NOT NULL
);
CREATE TABLE books(
	id SERIAL PRIMARY KEY,
	title VARCHAR(100) NOT NULL,
	isbn INT NOT NULL,
	date_publication DATE NOT NULL
);
CREATE TABLE books_compositions(
	id SERIAL PRIMARY KEY,
	book_id INT,
	composition_id INT,
	FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
	FOREIGN KEY (composition_id) REFERENCES compositions(id) ON DELETE CASCADE,
	UNIQUE(book_id, composition_id)
);

CREATE TABLE library_books(
	id SERIAL PRIMARY KEY,
	status VARCHAR(100) NOT NULL,
	book_id INT,
	FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
	UNIQUE(book_id)
);
CREATE TABLE journals(
	id SERIAL PRIMARY KEY,
	date_of_issue DATE NOT NULL,
	date_return DATE NOT NULL,
	library_book_id INT,
	reader_id INT,
	FOREIGN KEY (library_book_id) REFERENCES library_books(id) ON DELETE CASCADE,
	FOREIGN KEY (reader_id) REFERENCES readers(id) ON DELETE CASCADE,
	UNIQUE(library_book_id, reader_id)
);