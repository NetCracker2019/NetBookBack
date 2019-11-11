package com.example.netbooks;

import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetbooksApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(NetbooksApplication.class, args);
	}
	@Autowired
	BookService searchBook;
	@Autowired
	JdbcBookRepository jdbcBookRepository;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(searchBook.filterBooks("", "a", "Detective", "1800-01-01", "1940-01-01", 1, 500));
	}
}

