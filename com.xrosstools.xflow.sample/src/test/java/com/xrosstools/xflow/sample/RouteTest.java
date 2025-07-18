package com.xrosstools.xflow.sample;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;

public class RouteTest extends TestAdapter {
	@Test
	public void testMergeEarly() throws Exception {
		Xflow f = Routes.MergeEarly.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		f.start(context);

		while(!f.isEnded())
			sleep1();

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(50, counter.get());
	}
}
