package com.example.demo.dto;

public class SearchDto {

	private String field;

	private String value;

	private String facetField;

	private boolean fullText;

	private String query;

	public SearchDto(String field, String value, String facetField, boolean fullText, String query) {
		this.field = field;
		this.value = value;
		this.facetField = facetField;
		this.fullText = fullText;
		this.query = query;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFacetField() {
		return facetField;
	}

	public void setFacetField(String facetField) {
		this.facetField = facetField;
	}

	public boolean isFullText() {
		return fullText;
	}

	public void setFullText(boolean fullText) {
		this.fullText = fullText;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
