package com.example.demo.mapper;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.epub.EpubBook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

	public Book epubToEntity(EpubBook epubBook) {
		return new Book(epubBook.getId(), epubBook.getTitle(), epubBook.getAuthors(),
				epubBook.getContent(), epubBook.getLanguage());
	}

	public List<Book> epubToEntity(List<EpubBook> epubBooks) {
		return epubBooks.stream()
				.map(this::epubToEntity)
				.collect(Collectors.toList());
	}

	public BookDto entityToDto(Book bookEntity) {
		return new BookDto(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthors(),
				bookEntity.getContent(), bookEntity.getLanguage());
	}

	public List<BookDto> entityToDto(List<Book> bookEntities) {
		return bookEntities.stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
	}
}
