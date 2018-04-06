package com.springboot.elasticsearch.bussiness.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.springboot.elasticsearch.TDDBaseTestSuit;
import com.springboot.elasticsearch.bussiness.BookService;
import com.springboot.elasticsearch.common.OperationType;
import com.springboot.elasticsearch.entity.Book;

public class BookServiceImplTest extends TDDBaseTestSuit {

	@Autowired
	private BookService bookService;

	@Before
	public void before() {
//		esTemplate.deleteIndex(Book.class);
//		esTemplate.createIndex(Book.class);
//		esTemplate.putMapping(Book.class);
//		esTemplate.refresh(Book.class);
	}

	@Test
	public void testSave() throws Exception {

		Book sourceBook = new Book("1001", "Elasticsearch", "Elasticsearch Basics", "Rambabu Posa",
				"Use lucene query data...", "23-FEB-2017");
		Book destBook = bookService.save(sourceBook);

		assertNotNull(destBook.getId());
		assertEquals(destBook.getTitle(), sourceBook.getTitle());
		assertEquals(destBook.getAuthor(), sourceBook.getAuthor());
		assertEquals(destBook.getReleaseDate(), sourceBook.getReleaseDate());

	}

	@Test
	public void testFindOne() {

		Book book = new Book("1001", "Elasticsearch", "Elasticsearch Basics", "Rambabu Posa",
				"Use lucene query data...", "23-FEB-2017");
		bookService.save(book);

		Book testBook = bookService.findOne(book.getId());

		assertNotNull(testBook.getId());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

	}

	@Test
	public void testFindByTitle() {

		Book book = new Book("1001", "Elasticsearch", "Elasticsearch Basics", "Rambabu Posa",
				"Use lucene query data...", "23-FEB-2017");
		bookService.save(book);

		List<Book> byTitle = bookService.findByTitle(book.getTitle());
		assertThat(byTitle.size(), is(1));
	}

	@Test
	public void testFindByAuthor() {

		List<Book> bookList = new ArrayList<>();

		bookList.add(new Book("1001", "Elasticsearch", "Elasticsearch Basics", "Rambabu Posa",
				"Use lucene query data...", "23-FEB-2017"));
		bookList.add(new Book("1002", "Apache Lucene", "Apache Lucene Basics", "Rambabu Posa",
				"1002-Lucene inverse index query...", "13-MAR-2017"));
		bookList.add(new Book("1003", "Apache Solr", "Apache Solr Basics", "Rambabu Posa",
				"1003-Solr inverse index ...", "21-MAR-2017"));
		bookList.add(new Book("1007", "Spring Data", "Spring Data + ElasticSearch", "Rambabu Posa",
				"Spring boot + ElasticsearchRepository..", "01-APR-2017"));
		bookList.add(
				new Book("1008", "MongoDB", "Spring Boot + MongoDB", "Roger", "NoSQL---MongoDB...", "25-FEB-2017"));

		for (Book book : bookList) {
			bookService.save(book);
		}

		Page<Book> byAuthor = bookService.findByAuthor("Rambabu Posa", new PageRequest(0, 10));
		assertThat(byAuthor.getTotalElements(), is(4L));

		Page<Book> byAuthor2 = bookService.findByAuthor("Roger", new PageRequest(0, 10));
		assertThat(byAuthor2.getTotalElements(), is(1L));

	}

	@Test
	public void testDelete() {

		Book book = new Book("1001", "Elasticsearch", "Elasticsearch Basics", "Rambabu Posa",
				"Use lucene query data...", "23-FEB-2017");
		bookService.save(book);
		bookService.delete(book);
		Book testBook = bookService.findOne(book.getId());
		assertNull(testBook);
	}

	@Test
	public void testSearchBooks() {
		String field = "author";
		String value = "Rambabu Posa";
		
		List<Book> books = bookService.searchBooks(field, value, OperationType.MUST);
		for (Book book : books) {
			System.out.println(book.toString());
		}
	}
}
