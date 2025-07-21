package com.xrosstools.xflow.sample;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;

public class RouteTest extends TestAdapter {
	@Test
	public void testParallelCase1() throws Exception {
		Xflow f = Routes.ParallelCase1.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		f.start(context);

		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(50, counter.get());
	}

	@Test
	public void testParallelCase2() throws Exception {
		Xflow f = Routes.ParallelCase2.create();
		XflowContext context = new XflowContext();
		context.put(TestAddOne.PROP_KEY_COUNTER, new AtomicInteger(0));
		f.start(context);

		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(5, counter.get());
	}

	@Test
	public void testParallelCase3() throws Exception {
		Xflow f = Routes.ParallelCase3.create();
		XflowContext context = new XflowContext();
		context.put(TestAddOne.PROP_KEY_COUNTER, new AtomicInteger(0));
		f.start(context);

		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(24, counter.get());
	}

	@Test
	public void testParallelCase4() throws Exception {
		Xflow f = Routes.ParallelCase4.create();
		XflowContext context = new XflowContext();
		context.put(TestAddOne.PROP_KEY_COUNTER, new AtomicInteger(0));
		f.start(context);

		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(8, counter.get());
	}

	@Test
	public void testInclusiveCase1() throws Exception {
		Map<String, Integer> data = new HashMap<>();
		data.put("r1", 2);
		data.put("r2", 3);
		data.put("r3", 4);

		data.put("r1,r2", 4);
		data.put("r1,r3", 6);
		data.put("r3,r2", 7);
		data.put("r1,r3,r2", 8);
		for(String pathes: data.keySet()) {
			int value = data.get(pathes);
			Xflow f = Routes.InclusiveCase1.create();
			
			XflowContext context = new XflowContext();
			context.put(TestInclusiveRouter.PROP_KEY_PATHES, pathes);
			context.put(TestAddOne.PROP_KEY_COUNTER, new AtomicInteger(0));
	
			f.start(context);
			waitToEnd(f);
	
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(value, counter.get());
		}
	}

	@Test
	public void testInclusiveCase2() throws Exception {
		Map<String, Integer> data = new HashMap<>();
		data.put("r1", 2);
		data.put("r2", 3);

		data.put("r1,r2", 5);
		for(String pathes: data.keySet()) {
			int value = data.get(pathes);
			Xflow f = Routes.InclusiveCase2.create();
			
			XflowContext context = new XflowContext();
			context.put(TestInclusiveRouter.PROP_KEY_PATHES, pathes);
			context.put(TestAddOne.PROP_KEY_COUNTER, new AtomicInteger(0));
	
			f.start(context);
			waitToEnd(f);
	
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(value, counter.get());
		}
	}
	
	public void waitToEnd(Xflow f) throws Exception {
		int i = 0;
		while(!f.isEnded()) {
			sleep1();
			i++;
			if(i > 1000) {
				i = 0;
				System.out.println(f.getActiveNodeIds());
			}
		}
	}
}
