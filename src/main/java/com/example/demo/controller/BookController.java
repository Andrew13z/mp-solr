package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import com.example.demo.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping("/init")
	public ResponseEntity<String> initIndexing() {
		bookService.initIndexing();
		return ResponseEntity.ok("Indexing started");
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDto> findById(@PathVariable String id) {
		return ResponseEntity.ok(bookService.findById(id));
	}
}

