package com.springboot.elasticsearch.javaconfig;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.springboot.elasticsearch.repository")
public class EsConfig {

	@Value("${elasticsearch.host}")
	private String esHost;

	@Value("${elasticsearch.port}")
	private int esPort;

	@Value("${elasticsearch.clustername}")
	private String esClusterName;

	@Bean
	public Client client() throws Exception {
		Settings esSettings = Settings.settingsBuilder().put("cluster.name", esClusterName).build();
		
		TransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName(esHost),esPort);
		
		return TransportClient.builder().settings(esSettings).build().addTransportAddress(transportAddress);
	}
	
	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() throws Exception{
		return new ElasticsearchTemplate(client());
	}
	
}
