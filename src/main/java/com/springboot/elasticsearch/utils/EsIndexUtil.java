package com.springboot.elasticsearch.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsIndexUtil {

	private static Logger logger = LoggerFactory.getLogger(EsIndexUtilTest.class);

	private static String esHost = "127.0.0.1";

	private static int esPort = 9300;

	private static Client getClient() {
		try {
			return TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static void createIndex(String indexName) {
		if (indexIsExists(indexName)) {
			logger.info(indexName + " - 索引已经存在.");
			return;
		}
		
		CreateIndexRequest request = new CreateIndexRequest(indexName);
		
		CreateIndexResponse response = getClient().admin().indices().create(request).actionGet();
		
		if (!response.isAcknowledged())
			logger.info(indexName + " - 索引创建失败.");

		logger.info(indexName + " - 索引创建成功.");
		
	}

	public static void deleteIndex(String indexName) {
		if (!indexIsExists(indexName)){
			logger.info(indexName + " - 索引不存在.");
			return;
		}

		DeleteIndexResponse response = getClient().admin().indices().prepareDelete(indexName).execute().actionGet();

		if (!response.isAcknowledged())
			logger.info(indexName + " - 索引删除失败.");

		logger.info(indexName + " - 索引删除成功.");
	}

	public static boolean indexIsExists(String indexName) {
		// step1 ：索引存在请求
		IndicesExistsRequest request = new IndicesExistsRequest(indexName);
		// step2 ：索引存在响应
		IndicesExistsResponse response = getClient().admin().indices().exists(request).actionGet();
		// step3 ：得到结果
		return response.isExists();
	}
}
