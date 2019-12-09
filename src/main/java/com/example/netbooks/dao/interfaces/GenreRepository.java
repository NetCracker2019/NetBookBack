package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Book;
import com.example.netbooks.models.Genre;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface GenreRepository {
    public List<String> getAllGenreNames();
    public List<Genre> getAllGenres();
    public String addRowIntoBookGenre(String title, String description, List<String> id);
}
