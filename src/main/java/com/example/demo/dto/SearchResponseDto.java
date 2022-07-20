package com.example.demo.dto;

import org.springframework.data.solr.core.query.result.FacetFieldEntry;

import java.util.List;

public class SearchResponseDto {

	private List<BookDto> books;

	private List<FacetFieldEntry> facets;

	private long numFound;

	public List<BookDto> getBooks() {
		return books;
	}

	public void setBooks(List<BookDto> books) {
		this.books = books;
	}

	public List<FacetFieldEntry> getFacets() {
		return facets;
	}

	public void setFacets(List<FacetFieldEntry> facets) {
		this.facets = facets;
	}

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}
}
