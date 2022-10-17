package com.example.demo.epub;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Identifier;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

@Component
public class EpubExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EpubExtractor.class);
	private final EpubReader epubReader;

	@Autowired
	public EpubExtractor(EpubReader epubReader) {
		this.epubReader = epubReader;
	}

	public List<EpubBook> getAllBooksFromFolder(String location) {
        try (Stream<Path> paths = Files.walk(Paths.get(location))) {
            return paths
                .filter(((Predicate<Path>) Files::isRegularFile).and(path -> path.toString().toLowerCase().endsWith(".epub")))
				.map(this::readEpub)
				.flatMap(Optional::stream)
				.map(this::toEpubBook)
				.collect(toList());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read files from " + location, e);
        }
	}

    private Optional<Book> readEpub(Path path){
        try (var book = Files.newInputStream(path)){
            return Optional.ofNullable(epubReader.readEpub(book));
        } catch (IOException e) {
            LOGGER.error("Error while reading book {}", path.getFileName());
		}
        return Optional.empty();
	}

	private EpubBook toEpubBook(Book book) {
        var id = getId(book.getMetadata().getIdentifiers());

		var title = book.getTitle();

		LOGGER.info("Working on book with id '{}' and title '{}'", id, title);

		var authors = book.getMetadata().getAuthors().stream()
				.map(Author::toString)
				.collect(toList());

		var content = getText(book);

		var language = book.getMetadata().getLanguage();

		return new EpubBook(id, title, authors, content, language);
	}

	private String getText(Book book) {
		var spine = new Spine(book.getTableOfContents());
		return spine.getSpineReferences().stream()
				.map(SpineReference::getResource)
				.map(this::getText)
				.collect(Collectors.joining());
	}

    private String getId (List<Identifier> identifiers) {
		return identifiers.stream()
			.filter(i -> i.getScheme().equals("URI"))
			.findFirst()
			.map(Identifier::getValue)
            .map(s -> s.substring(s.lastIndexOf("/") + 1))
			.orElseGet(() -> UUID.randomUUID().toString().substring(0, 5));
    }

	private String getText(nl.siegmann.epublib.domain.Resource resource) {
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
