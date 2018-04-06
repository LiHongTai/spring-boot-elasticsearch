package com.springboot.elasticsearch.utils;

import org.junit.Assert;
import org.junit.Test;

import com.springboot.elasticsearch.TDDBaseTestSuit;

public class EsIndexUtilTest extends TDDBaseTestSuit {

	@Test
	public void testIndexIsExists() {
		boolean flag = EsIndexUtil.indexIsExists("elasticsearch");
		Assert.assertTrue(flag);
	}
	
	@Test
	public void testDeleteIndex() {
		EsIndexUtil.deleteIndex("elasticsearch");
	}
}
