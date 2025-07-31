package com.xrosstools.xflow.sample.spring;

import org.springframework.stereotype.Component;

@Component
public class TestActivityConfig {
	private int count = 10;
	private String key = "counter";

	public int getCount() {
		return count;
	}

	public String getKey() {
		return key;
	}
}
