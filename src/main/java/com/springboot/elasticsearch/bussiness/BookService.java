package com.springboot.elasticsearch.bussiness;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.springboot.elasticsearch.common.OperationType;
import com.springboot.elasticsearch.entity.Book;

public interface BookService {

	public Book save(Book book);
	
	public void delete(Book book);
	
	public Book findOne(String id);
	
	public List<Book> findAll();
	
	public Page<Book> findByAuthor(String author,PageRequest pageRequest);
	
	public List<Book> findByTitle(String title);

	public List<Book> searchBooks(String field, String value,OperationType operationType);
}
