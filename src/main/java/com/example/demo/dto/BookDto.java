package com.example.demo.dto;

import java.util.List;

public class BookDto {

	private String id;

	private String title;

	private List<String> authors;

	private String content;

	private String language;

	public BookDto(String id, String title, List<String> authors, String content, String language) {
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.content = content;
		this.language = language;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
