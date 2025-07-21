package com.xrosstools.xflow.sample;

import java.util.concurrent.atomic.AtomicInteger;

import com.xrosstools.xflow.BinaryRouter;
import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.NodeConfigAware;
import com.xrosstools.xflow.XflowContext;

public class TestGreaterThanRouter extends TestAdapter implements BinaryRouter, NodeConfigAware {
	public static final String PROP_KEY_QUANTITY_FIELD = "quantity field";
	public static final String PROP_KEY_THRESHOLD = "threshold";
	
	public String quantiryField;
	public int threshold;

	@Override
	public boolean route(XflowContext context) {
		call(context);
		
		AtomicInteger field = context.get(quantiryField);
		
		return field.get() > threshold;
	}

	@Override
	public void initNodeConfig(DataMap config) {
		quantiryField = config.get(PROP_KEY_QUANTITY_FIELD);
		threshold = config.get(PROP_KEY_THRESHOLD);
	}
}
