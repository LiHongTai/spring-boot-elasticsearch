package com.springboot.elasticsearch.bussiness.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.springboot.elasticsearch.bussiness.BookService;
import com.springboot.elasticsearch.common.OperationBuilderFactory;
import com.springboot.elasticsearch.common.OperationBuilderFactory.Builder;
import com.springboot.elasticsearch.common.OperationType;
import com.springboot.elasticsearch.entity.Book;
import com.springboot.elasticsearch.repository.BookRepository;

@Component(value = "bookService")
public class BookServiceImpl implements BookService {

	private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
	public static final String INDEX_NAME = "library";
	public static final String TYPE_NAME = "books";

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private Client client;

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void delete(Book book) {
		bookRepository.delete(book);
	}

	@Override
	public Book findOne(String id) {
		return bookRepository.findOne(id);
	}

	@Override
	public List<Book> findAll() {
		return (List<Book>) bookRepository.findAll();
	}

	@Override
	public Page<Book> findByAuthor(String author, PageRequest pageRequest) {
		return bookRepository.findByAuthor(author, pageRequest);
	}

	@Override
	public List<Book> findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	@Override
	public List<Book> searchBooks(String field, String value,OperationType operationType) {

		List<Book> books = new ArrayList<>();

		Builder builder = OperationBuilderFactory.builder().queryString(field, value, operationType);

		SearchResponse response = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(builder.builder())
				.execute().actionGet();
		
		logger.info(builder.builder().toString());
		
		SearchHits hits = response.getHits();
		for (SearchHit searchHit : hits) {
			Book book = new Book();
			Map<String, Object> sourceMap = searchHit.getSource();
			book.setId((String) sourceMap.get("id"));
			book.setName((String) sourceMap.get("name"));
			books.add(book);
		}

		return books;
	}
}
