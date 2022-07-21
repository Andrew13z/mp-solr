package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.SearchDto;
import com.example.demo.dto.SearchResponseDto;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.springframework.data.domain.Pageable;

public interface BookService {

	void initIndexing();

	BookDto findById(String id);

	SearchResponseDto findByQuery(SearchDto searchDto, Pageable pageable);

	SuggesterResponse suggest(String query);
}
