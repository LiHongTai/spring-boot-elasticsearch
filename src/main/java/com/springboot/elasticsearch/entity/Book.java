package com.springboot.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "library", type = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	private String id;

	private String name;

	private String title;

	private String author;

	private String description;

	private String releaseDate;

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", title=" + title + ", author=" + author + ", description="
				+ description + ", releaseDate=" + releaseDate + "]";
	}

}
