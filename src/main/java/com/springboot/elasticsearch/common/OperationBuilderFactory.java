package com.springboot.elasticsearch.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeohashCellQuery;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.util.CollectionUtils;

public class OperationBuilderFactory {

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Map<OperationType, List<QueryBuilder>> queryBuilderMap = new ConcurrentHashMap<>();
		/**
		 * 
		 * @param field				= 数据库字段名即列名
		 * @param value				= IK分词器处理后的值   即列值被拆分后的值，英文注意大小写问题
		 * 					可以用网址：http://localhost:9200/library/_analyze?analyzer=ik&pretty=true&text=我是中国人
		 *					来查看具体分词结果
		 * 						我		中国人	中国		国人
		 * 							http://localhost:9200/library/_analyze?analyzer=ik&pretty=true&text=Rambabu%20Posa
		 *                  来查看具体分词结果
		 *                            rambabu posa
		 *       只有当 value = rambabu or posa 时 才能查到结果
		 *       即当value精确匹配分词结果时才能查询到结果
		 * @param operationType
		 * @return
		 */
		public Builder term(String field, Object value, OperationType operationType) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders.add(new TermQueryBuilder(field, value));
			return this;
		}
		/**
		 * 
		 * @param field				= 数据库字段名即列名
		 * @param value				= IK分词器处理后的值   即列值被拆分后的值，英文注意大小写问题
		 * 					可以用网址：http://localhost:9200/library/_analyze?analyzer=ik&pretty=true&text=Rambabu%20Posa
		 *                  来查看具体分词结果
		 *                            rambabu posa
		 *       当 value = rambabu,posa,rambabu posa,Rambabu,Posa,Rambabu 1,...
		 *       即当value值包含分词结果中的任一个或者整个词都能查询到结果
		 * @param operationType
		 * @param operator
		 * @return
		 */
		public Builder queryString(String field, String value, OperationType operationType,
				QueryStringQueryBuilder.Operator operator) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders.add(new QueryStringQueryBuilder(value).field(field).defaultOperator(operator));
			return this;
		}

		public Builder queryString(String field, String value, OperationType operationType) {
			return queryString(field, value, operationType, Operator.OR);
		}
		/**
		 * 
		 * @param field				= 数据库字段名即列名
		 * @param prefix				= IK分词器处理后的值   即列值被拆分后的值，英文注意大小写问题
		 * 					可以用网址：http://localhost:9200/library/_analyze?analyzer=ik&pretty=true&text=Rambabu%20Posa
		 *                  来查看具体分词结果
		 *                            rambabu posa
		 *            当 prefix = rambabu,posa,r,ram,p,po,...
		 *            即当匹配值和任一分词的前缀相同可以查询到结果
		 * @param operationType
		 * @return
		 */
		public Builder prefix(String field, String prefix, OperationType operationType) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders.add(new PrefixQueryBuilder(field, prefix));
			return this;
		}

		public Builder range(String field, Object from, Object to, OperationType operationType) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders.add(new RangeQueryBuilder(field).from(from).to(to));
			return this;
		}
		
		/**
		 * 
		 * @param field				= 数据库字段名即列名
		 * @param value				= IK分词器处理后的值   即列值被拆分后的值，英文注意大小写问题
		 * 					可以用网址：http://localhost:9200/library/_analyze?analyzer=ik&pretty=true&text=Rambabu%20Posa
		 *                  来查看具体分词结果
		 *                            rambabu posa
		 *              当 value = rambabu,posa,ram*,ram*b*,p*,p*s*,*am*b*,....
		 *              即和分词同位置的词相同，其他用*代替，可以得到结果
		 * @param operationType
		 * @return
		 */
		public Builder wildcard(String field, String value, OperationType operationType) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders.add(new WildcardQueryBuilder(field, value));
			return this;
		}

		public Builder geoDistance(String field, double lat, double lon, double distance, OperationType operationType) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders
					.add(new GeoDistanceQueryBuilder(field).point(lat, lon).distance(distance, DistanceUnit.METERS));
			return this;
		}

		public Builder geoHash(String field, double lat, double lon, int precisionLevel, OperationType operationType) {
			List<QueryBuilder> queryBuilders = getQueryBuilders(operationType);
			queryBuilders
					.add(new GeohashCellQuery.Builder(field).point(lat, lon).precision(precisionLevel).neighbors(true));
			return this;
		}

		public QueryBuilder builder() {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			
			List<QueryBuilder> mustBuilders = getQueryBuilders(OperationType.MUST);
			if(!CollectionUtils.isEmpty(mustBuilders)) {
				for (QueryBuilder queryBuilder : mustBuilders) {
					boolQueryBuilder.must(queryBuilder);
				}
			}
			
			List<QueryBuilder> mustNotBuilders = getQueryBuilders(OperationType.MUST_NOT);
			if(!CollectionUtils.isEmpty(mustNotBuilders)) {
				for (QueryBuilder queryBuilder : mustNotBuilders) {
					boolQueryBuilder.mustNot(queryBuilder);
				}
			}
			
			List<QueryBuilder> shouldBuilders = getQueryBuilders(OperationType.SHOULD);
			if(!CollectionUtils.isEmpty(shouldBuilders)) {
				for (QueryBuilder queryBuilder : shouldBuilders) {
					boolQueryBuilder.should(queryBuilder);
				}
			}
			
			return boolQueryBuilder;
		}
		
		public List<QueryBuilder> getQueryBuilders(OperationType operationType) {
			List<QueryBuilder> queryBuilders = queryBuilderMap.get(operationType);
			if (queryBuilders == null) {
				synchronized (this) {
					if (queryBuilders == null) {
						queryBuilders = new ArrayList<>();
						queryBuilderMap.put(operationType, queryBuilders);
					}
				}
			}
			return queryBuilders;
		}

	}
}
