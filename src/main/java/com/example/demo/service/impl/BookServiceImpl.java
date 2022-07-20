package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.SearchDto;
import com.example.demo.dto.SearchResponseDto;
import com.example.demo.epub.EpubExtractor;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.SearchMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

	public static final String BOOK_COLLECTION_NAME = "book";

	public static final String CONTENT_FIELD_NAME = "content";

	private final BookRepository bookRepository;

	private final EpubExtractor epubExtractor;

	private final BookMapper bookMapper;

	private final SearchMapper searchMapper;

	private final SolrTemplate solrTemplate;

	@Value("${app.book.files.location}")
	private String location;

	@Autowired
	public BookServiceImpl(BookRepository bookRepository, EpubExtractor epubExtractor,
						   BookMapper bookMapper, SearchMapper searchMapper, SolrTemplate solrTemplate) {
		this.bookRepository = bookRepository;
		this.epubExtractor = epubExtractor;
		this.bookMapper = bookMapper;
		this.searchMapper = searchMapper;
		this.solrTemplate = solrTemplate;
	}

	@Async
	public void initIndexing() {
		var epubBooks = epubExtractor.getAllBooksFromFolder(location);
		bookRepository.saveAll(bookMapper.epubToEntity(epubBooks));
		LOGGER.info("All books are saved.");
	}

	public BookDto findById(String id) {
		var book = bookRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Book not found by id: " + id));
		return bookMapper.entityToDto(book);
	}

	public SearchResponseDto findByQuery(SearchDto searchDto, Pageable pageable) {
		var criteria = createCriteria(searchDto);
		var query = createQuery(criteria, pageable, searchDto.getFacetField());
		return executeQuery(query, searchDto.getFacetField());
	}

	private Criteria createCriteria(SearchDto searchDto) {
		if (searchDto.isFullText()) {
			return new Criteria(CONTENT_FIELD_NAME).expression(searchDto.getQuery());
		}
		return new Criteria(searchDto.getField()).contains(searchDto.getValue());
	}

	private Query createQuery(Criteria criteria, Pageable pageable, String facetField) {
		if (facetField != null) {
			return new SimpleFacetQuery(criteria, pageable)
					.setFacetOptions(new FacetOptions().addFacetOnField(facetField));
		}
		return new SimpleQuery(criteria, pageable);
	}

	private SearchResponseDto executeQuery(Query query, String facetField) {
		if (facetField != null) {
			var result =
					solrTemplate.queryForFacetPage(BOOK_COLLECTION_NAME, (FacetQuery) query, BookDto.class);
			return searchMapper.toSearchDto(result, facetField);
		}
		var result = solrTemplate.queryForPage(BOOK_COLLECTION_NAME, query, BookDto.class);
		return searchMapper.toSearchDto(result);
	}
}
