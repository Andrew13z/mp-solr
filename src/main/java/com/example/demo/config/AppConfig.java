package com.example.demo.config;

import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public EpubReader epubReader() {
		return new EpubReader();
	}
}
