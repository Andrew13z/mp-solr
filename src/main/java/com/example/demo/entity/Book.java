package com.example.demo.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

@SolrDocument(collection = "book")
public class Book {

	@Id
	private String id;

	@Field
	private String title;

	@Field
	private List<String> authors;

	@Field
	private String content;

	@Field
	private String language;

	public Book(String id, String title, List<String> authors, String content, String language) {
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
