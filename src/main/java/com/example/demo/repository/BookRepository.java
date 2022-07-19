package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends SolrCrudRepository<Book, String> {

	Optional<Book> findByTitle(String title);

}
