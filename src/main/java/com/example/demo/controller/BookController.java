package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.SearchDto;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PostMapping
	public SearchResponseDto findByQuery(@RequestBody SearchDto searchDto,
										 @RequestParam(defaultValue = "0") int page,
										 @RequestParam(defaultValue = "10") int size) {
		return bookService.findByQuery(searchDto, PageRequest.of(page, size));
	}
}
