package com.example.netbooks.dao;

import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcBookRepository implements BookRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BookRowMapper());
    }

    @Override
    public List<Book> findBooksByTitle(String bookTitle) {
        bookTitle = "%" + bookTitle + "%";
        SqlParameterSource namedParameters = new MapSqlParameterSource("searchString", bookTitle);
        String sql = "select * from book where lower(title) like :searchString";
        return namedParameterJdbcTemplate.query(sql, namedParameters, new BookRowMapper());
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("searchString", "%" + author + "%");
        String sql = "select * from book inner join book_author using (book_id) where author_id = \n" +
                "(select author_id from author where lower(firstname) like :searchString or lower(lastname) like :searchString)";

        return namedParameterJdbcTemplate.query(sql, namedParameters, new BookRowMapper());
    }

    @Override
    public List<Book> findBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", "%"+author+"%");
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("date1", date1);
        namedParameters.addValue("date2", date2);
        namedParameters.addValue("page1", page1);
        namedParameters.addValue("page2", page2);
        String sql = "All".equals(genre) ?
                "select * from book " +
                "inner join book_author using (book_id) " +
                "where lower(title) like :title " +
                "and author_id in " +
                "(select author_id from author where lower(firstname) like :author or lower(lastname) like :author) " +
                "and release_date between :date1 and :date2 " +
                "and pages between :page1 and :page2"
                :
                "select * from book " +
                "inner join book_author using (book_id) " +
                "inner join book_genre using (book_id) " +
                "where lower(title) like :title " +
                "and author_id in " +
                "(select author_id from author where lower(firstname) like :author or lower(lastname) like :author) " +
                "and genre_id in " +
                "(select genre_id from genre where genre_name = :genre) " +
                "and release_date between :date1 and :date2 " +
                "and pages between :page1 and :page2";

        return namedParameterJdbcTemplate.query(sql, namedParameters, new BookRowMapper());
    }

    @Override
    public void save(Book book) {
    }

}
class BookRowMapper implements RowMapper{
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book(
                resultSet.getLong("book_id"),
                resultSet.getString("title"),
                resultSet.getString("image_path"),
                resultSet.getDate("release_date"),
                resultSet.getString("lang"),
                resultSet.getInt("pages"),
                resultSet.getBoolean("approved"));
        return book;
    }
}