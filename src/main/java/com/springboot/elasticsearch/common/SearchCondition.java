package com.springboot.elasticsearch.common;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

public class SearchCondition {

	private QueryBuilder queryBuilder = null;
	private QueryBuilder filterBuilder = null;
	private List<SortBuilder> orders = new ArrayList<>();
	private List<AbstractAggregationBuilder> aggregationBuilders = new ArrayList<>();

	private SearchType searchType;
	private int limit;
	private int offset;
	private int total = 0;

	public QueryBuilder getQueryBuilder() {
		if (queryBuilder == null)
			queryBuilder = QueryBuilders.matchAllQuery();
		return queryBuilder;
	}

	public SearchCondition setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
		return this;
	}

	public QueryBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public SearchCondition setFilterBuilder(QueryBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
		return this;
	}

	public List<SortBuilder> getOrders() {
		return orders;
	}

	public void setOrders(List<SortBuilder> orders) {
		this.orders = orders;
	}

	public List<AbstractAggregationBuilder> getAggregationBuilders() {
		return aggregationBuilders;
	}

	public void setAggregationBuilders(List<AbstractAggregationBuilder> aggregationBuilders) {
		this.aggregationBuilders = aggregationBuilders;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public SearchCondition setSearchType(SearchType searchType) {
		this.searchType = searchType;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public SearchCondition setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public int getOffset() {
		return offset;
	}

	public SearchCondition setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public int getTotal() {
		return total;
	}

	public SearchCondition setTotal(int total) {
		this.total = total;
		return this;
	}
}
