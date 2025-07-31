package com.xrosstools.xflow.sample.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.XflowContext;

@Component
public class TestSpringAutoActivity implements AutoActivity {
	@Autowired
	private TestActivityConfig config;

	@Override
	public void execute(XflowContext context) {
		int counter = context.get(config.getKey());
		context.put(config.getKey(), counter + config.getCount());
	}
}
