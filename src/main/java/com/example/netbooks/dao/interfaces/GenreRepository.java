package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository {
    List<String> getAllGenreNames();
    List<Genre> getAllGenres();
    String addRowIntoBookGenre(String title, List<String> id);
}
