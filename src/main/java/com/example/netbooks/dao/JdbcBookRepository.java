package com.example.netbooks.dao;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.dao.interfaces.BookRepository;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ViewBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcBookRepository implements BookRepository {
    private final Logger logger = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int getAmountOfAnnouncement() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM announcement;", Integer.class);
    }

    @Override
    public List<Book> findAllBooks() {
        return null;
    }

    @Override
    public List<Book> findBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2) {
        return null;
    }

    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true", this::mapRowToAnnouncement);
    }

    @Override
    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
//        logger.info(jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY title LIMIT 5 OFFSET 1", this::mapRowToAnnouncement));
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY announcment_id LIMIT " + booksPerPage + " OFFSET " + startIndex, this::mapRowToAnnouncement);
    }

    @Override
    public List<ViewBook> findAllViewBooks() {
        return null;
    }

    @Override
    public List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor) {
        return null;
    }

    @Override
    public List<ViewBook> findViewBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2) {
        return null;
    }

    @Override
    public ViewBook getBookById(int id) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", this::mapRowToBook);
    }


    @Override
    public String addBook(Book book) {
        int isThisBookExist = jdbcTemplate.queryForObject("select count(*) from book where title='" + book.getTitle() + "'", Integer.class);
        if(isThisBookExist == 0){
            jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, description, approved) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?, ?)",
                    new Object[] {book.getTitle(), 0, book.getImagePath(), book.getRelease_date(), book.getLanguage(), book.getPages(), book.getDescription(), false});

            String[] subStrAuthors = book.getAuthor().split(", ");
            for (int i = 0; i < subStrAuthors.length; i++) {
                int isThisAuthorExist = jdbcTemplate.queryForObject("select count(*) from author where fullname='" + subStrAuthors[i] + "'", Integer.class);
                if(isThisAuthorExist == 0){
                    jdbcTemplate.update("insert into author (fullname) values (?)", new Object[] {subStrAuthors[i]});
                }
                jdbcTemplate.update("insert into book_author values ((select book_id from book where title ='" + book.getTitle() + "'), \n" +
                        "\t\t   (select author_id from author where fullname ='" +  subStrAuthors[i] + "'))");
            }

            String[] subStrGenres = book.getGenre().split(", ");
            for (int i = 0; i < subStrGenres.length; i++) {
                int isThisGenreExist = jdbcTemplate.queryForObject("select count(*) from genre where genre_name='" + subStrGenres[i] + "'", Integer.class);
                if(isThisGenreExist == 0){
                    jdbcTemplate.update("insert into genre (genre_name) values (?)", new Object[] {subStrGenres[i]});
                }
                jdbcTemplate.update("insert into book_genre values ((select book_id from book where title ='" + book.getTitle() + "'), \n" +
                        "\t\t   (select genre_id from genre where genre_name ='" +  subStrGenres[i] + "'))");
            }
        }
        return "Complete!";
    }


    private String mapRowGenresToBook(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString("genre_name");
    }

    private String mapRowAuthorsToBook(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString("fullname");
    }

    private Book mapRowToBook(ResultSet resultSet, int i) throws SQLException {
        String arrayOfGenre = "" + jdbcTemplate.query("select  book.title, genre.genre_name\n" +
                "from book_genre\n" +
                "join genre on book_genre.genre_id = genre.genre_id\n" +
                "join book on book_genre.book_id = book.book_id where book.title ='" + resultSet.getString("title") + "'", this::mapRowGenresToBook);

        String arrayOfAuthors = "" + jdbcTemplate.query("select  book.title, author.fullname\n" +
                "from book_author\n" +
                "join author on book_author.author_id = author.author_id\n" +
                "join book on book_author.book_id = book.book_id where book.title ='" + resultSet.getString("title") + "'", this::mapRowAuthorsToBook);

        return new Book(
                resultSet.getLong("book_id"),
                resultSet.getString("title"),
                arrayOfAuthors,
                arrayOfGenre,
                resultSet.getInt("likes"),
                resultSet.getString("image_path"),
                resultSet.getString("release_date"),
                resultSet.getString("lang"),
                resultSet.getInt("pages"),
                resultSet.getString("description"),
                resultSet.getBoolean("approved"));
    }

    private Announcement mapRowToAnnouncement(ResultSet resultSet, int i) throws SQLException {
        return new Announcement(
                resultSet.getInt("announcment_id"),
                resultSet.getInt("announcement_book_id"),
                resultSet.getInt("user_id"),
                resultSet.getBoolean("approved"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("image_path"));
    }
}
