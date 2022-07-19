package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableSolrRepositories
@EnableAsync
public class MpSolrApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpSolrApplication.class, args);
	}

}
