package com.example.demo.service;

import com.example.demo.dto.BookDto;

public interface BookService {

	void initIndexing();

	BookDto findById(String id);
}
