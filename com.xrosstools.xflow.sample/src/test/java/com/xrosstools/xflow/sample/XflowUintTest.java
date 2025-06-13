package com.xrosstools.xflow.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.EventSpec;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;

public class XflowUintTest {
	@Test
	public void testAutoActivity() throws Exception {
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		f.start(context);
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
	}

	@Test
	public void testTaskActivity() throws Exception {
		Xflow f = UnitTest.TaskActivity.create();
		XflowContext context = new XflowContext();
		f.start(context);
		sleep();
		List<Task> tasks = f.getTasks("Jerry");
		assertEquals(5, tasks.size());
		FeedbackTask task = (FeedbackTask)tasks.get(0);
		task.setFeedback("NO");
		f.submit(task);

		sleep();
		assertFalse(f.isEnded());

		task = (FeedbackTask)tasks.get(1);
		task.setFeedback("OK");
		f.submit(task);
		
		sleep();
		assertTrue(f.isEnded());
		assertTrue(f.getTasks("Jerry").isEmpty());
	}
	
	@Test
	public void testEventActivity() throws Exception {
		Xflow f = UnitTest.EventActivity.create();
		XflowContext context = new XflowContext();
		f.start(context);
		sleep();
		List<EventSpec> specs = f.getEventSpecs();
		assertEquals(1, specs.size());
		
		Event event = specs.get(0).create();
		f.notify(event);
		
		sleep();
		assertTrue(f.isEnded());
	}

	@Test
	public void testWaitActivity() throws Exception {
		Xflow f = UnitTest.WaitActivity.create();
		XflowContext context = new XflowContext();
		f.start(context);
		long dur = System.currentTimeMillis();
		waitToEnd(f);
		dur = System.currentTimeMillis() - dur;
		System.out.println(dur);
		assertTrue(dur > 100 && dur < 105);
	}

	@Test
	public void testBinaryRouterTrue() throws Exception {
		Xflow f = UnitTest.BinaryRouter.create();
		
		//True path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, true);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		f.start(context);
		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
	}
	
	@Test
	public void testBinaryRouterFalse() throws Exception {
		Xflow f = UnitTest.BinaryRouter.create();

		//False path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, false);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		f.start(context);
		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(30, counter.get());
	}

	private void sleep1() throws Exception {
		Thread.sleep(1);
	}

	private void sleep() throws Exception {
		Thread.sleep(5);		
	}
	
	private void waitToEnd(Xflow f) throws Exception {
		while(!f.isEnded())
			sleep1();
	}
}
