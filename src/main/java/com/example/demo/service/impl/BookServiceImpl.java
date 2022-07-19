package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.epub.EpubExtractor;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

	private final BookRepository bookRepository;
	private final EpubExtractor epubExtractor;

	private final BookMapper bookMapper;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository, EpubExtractor epubExtractor, BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.epubExtractor = epubExtractor;
		this.bookMapper = bookMapper;
	}

	@Async
	public void initIndexing() {
		var epubBooks = epubExtractor.getAllBooksFromFolder("classpath:/books/*.epub");
		bookRepository.saveAll(bookMapper.epubToEntity(epubBooks));
		LOGGER.info("All books are saved.");
	}

	public BookDto findById(String id) {
		var book = bookRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Book not found by id: " + id));
		return bookMapper.entityToDto(book);
	}

	public Book findByTitle(String title) {
		return bookRepository.findByTitle(title)
				.orElseThrow(() -> new NoSuchElementException("Book not found by title: " + title));
	}
}
