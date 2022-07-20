package com.example.demo.mapper;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Component;

@Component
public class SearchMapper {

	public SearchResponseDto toSearchDto(FacetPage<BookDto> page, String facetField) {
		var searchResponseDto = new SearchResponseDto();
		searchResponseDto.setBooks(page.getContent());
		searchResponseDto.setFacets(page.getFacetResultPage(facetField).getContent());
		searchResponseDto.setNumFound(page.getTotalElements());
		return searchResponseDto;
	}

	public SearchResponseDto toSearchDto(Page<BookDto> page) {
		var searchResponseDto = new SearchResponseDto();
		searchResponseDto.setBooks(page.getContent());
		searchResponseDto.setNumFound(page.getTotalElements());
		return searchResponseDto;
	}
}
