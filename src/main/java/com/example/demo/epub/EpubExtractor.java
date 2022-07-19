package com.example.demo.epub;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Component
public class EpubExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EpubExtractor.class);
	private final EpubReader epubReader;
	private final ResourcePatternResolver resourcePatternResolver;

	@Autowired
	public EpubExtractor(EpubReader epubReader, ResourcePatternResolver resourcePatternResolver) {
		this.epubReader = epubReader;
		this.resourcePatternResolver = resourcePatternResolver;
	}

	public List<EpubBook> getAllBooksFromFolder(String location) {
		var epubBooks = new ArrayList<EpubBook>();
		try {
			var resources = resourcePatternResolver.getResources(location);
			for (var resource : resources) {
				try (var inputStream = resource.getInputStream()) {
					var book = epubReader.readEpub(inputStream);
					epubBooks.add(toEpubBook(book, resource));
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while reading books from {}", location);
			throw new EpubException(e);
		}
		LOGGER.info("Successfully read {} books from {}.", epubBooks.size(), location);
		return epubBooks;
	}

	private EpubBook toEpubBook(Book book, Resource resource) {
		var id = Optional.ofNullable(resource.getFilename())
				.map(name -> name.replace(".epub", ""))
				.orElseGet(() -> UUID.randomUUID().toString().substring(0, 5));

		var title = book.getTitle();

		LOGGER.trace("Working on book with id '{}' and title '{}'", id, title);

		var authors = book.getMetadata().getAuthors().stream()
				.map(Author::toString)
				.collect(Collectors.toList());

		var content = getContent(book);

		var language = book.getMetadata().getLanguage();

		return new EpubBook(id, title, authors, content, language);
	}

	private String getContent(Book book) {
		var spine = new Spine(book.getTableOfContents());
		return spine.getSpineReferences().stream()
				.map(SpineReference::getResource)
				.map(this::getContent)
				.collect(Collectors.joining());
	}

	private String getContent(nl.siegmann.epublib.domain.Resource resource) {
		try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			return reader.lines()
					.map(line -> Jsoup.parse(line).body().text())
					.filter(not(String::isBlank))
					.collect(Collectors.joining());
		} catch (IOException e) {
			LOGGER.error("Error while reading a resource.");
			throw new EpubException("Failed to read a resource", e);
		}
	}
}
