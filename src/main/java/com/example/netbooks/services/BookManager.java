package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ShortBookDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookManager {
    @Autowired
    JdbcBookRepository bookRepository = new JdbcBookRepository();


    public List<ShortBookDescription> getFavouriteBooksByUserId(Long id, int cntBooks, int offset) {
        return bookRepository.getBooksByUserId(id, cntBooks, offset, "getFavouriteBooksByUserId");
    }

    public List<ShortBookDescription> getReadingBooksByUserId(long id, int cntBooks, int offset) {
        return bookRepository.getBooksByUserId(id, cntBooks, offset, "getReadingBooksByUserId");
    }

    public List<ShortBookDescription> getReadBooksByUserId(long id, int cntBooks, int offset) {
        return bookRepository.getBooksByUserId(id, cntBooks, offset, "getReadBooksByUserId");
    }
}
