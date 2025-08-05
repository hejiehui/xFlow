package com.xrosstools.xflow.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.xrosstools.xflow.Event;
import com.xrosstools.xflow.EventSpec;
import com.xrosstools.xflow.Task;
import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowStatus;

public class NormalTest {
	@Test
	public void testAutoActivity() throws Exception {
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		f.start(context);
		String instId = "abc";
		f.setInstanceId(instId);
		assertEquals(instId, f.getInstanceId());
		waitToEnd(f);
		assertTrue(f.isSucceed());
		assertTrue(f.getStatus() == XflowStatus.SUCCEED);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
		assertEquals(100, (int)context.get("globalB"));
		assertEquals("abc", (String)context.get("globalA"));
		assertTrue((boolean)context.get("gBool"));
	}

	@Test
	public void testTaskActivity() throws Exception {
		Xflow f = UnitTest.TaskActivity.create();
		XflowContext context = new XflowContext();
		f.start(context);
		sleep();
		List<Task> tasks = f.getTasks("Jerry");
		assertEquals(5, tasks.size());
		
		List<Task> nodeTasks = f.getNodeTasks("aaaabbb");
		assertEquals(5, nodeTasks.size());
		
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
		assertEquals(specs.get(0), f.getEventSpec("event activity"));
		
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

	@Test
	public void testExclusiveRouter() throws Exception {
		for(int i = 1; i <= 3; i++) {
			Xflow f = UnitTest.ExclusiveRouter.create();
			
			XflowContext context = new XflowContext();
			//p1, p2, p3
			context.put(TestExclusiveRouter.PROP_KEY_PATH, "p" + i);
			context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
	
			f.start(context);
			waitToEnd(f);
	
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(10 * (i + 1), counter.get());
		}
	}
	
	@Test
	public void testInclusiveRouter() throws Exception {
		Map<String, Integer> data = new HashMap<>();
		data.put("p1,p2", 40);
		data.put("p1,p3", 50);
		data.put("p3,p2", 60);
//		data.put("", 40);
		for(String pathes: data.keySet()) {
			int value = data.get(pathes);
			Xflow f = UnitTest.InclusiveRouter.create();
			
			XflowContext context = new XflowContext();
			context.put(TestInclusiveRouter.PROP_KEY_PATHES, pathes);
			context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
	
			f.start(context);
			waitToEnd(f);
	
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(value, counter.get());
		}
	}
	
	@Test
	public void testParallelRouter() throws Exception {
		Xflow f = UnitTest.ParallelRouter.create();
		
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		f.start(context);
		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(10+10+20+30, counter.get());
	}
	
	@Test
	public void testFlowAbort() throws Exception {
		Xflow f = UnitTest.ParallelRouter.create();
		
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		String reason = "abort";
		f.start(context);
		f.abort(reason);
		
		assertTrue(f.isAbort());
		assertEquals(reason, f.getAbortReason());

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertNotSame(10+10+20+30, counter.get());
	}
	
	@Test
	public void testSubflowActivity() throws Exception {
		Xflow f = UnitTest.SubflowActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestSubflowActivity.COUNT, 10);
		f.start(context);
		waitToEnd(f);
		int counter = context.get(TestSubflowActivity.COUNT);
		assertEquals(10+10+20+30, counter);
	}

	@Test
	public void testFailCase() throws Exception {
		Xflow f = UnitTest.FailCase.create();
		XflowContext context = new XflowContext();
		context.put(TestSubflowActivity.COUNT, 10);
		context.put(TestAddOne.PROP_KEY_COUNTER, new AtomicInteger(0));
		context.put(TestBinaryRouter.PROP_KEY_RESULT, true);
		context.put(TestExclusiveRouter.PROP_KEY_PATH, "1");
		context.put(TestInclusiveRouter.PROP_KEY_PATHES, "1");
		f.start(context);

		sleep();

		//Task activity
		List<Task> tasks = f.getTasks("Tom");
		assertEquals(1, tasks.size());
		FeedbackTask task = (FeedbackTask)tasks.get(0);
		task.setFeedback("OK");
		f.submit(task);

		//Event activity
		List<EventSpec> specs = f.getEventSpecs();
		assertEquals(1, specs.size());
		
		Event event = specs.get(0).create();
		f.notify(event);

		//Wait activity should still running
		assertTrue(f.isActive("4"));
		
		while(f.isFailed() == false)
			sleep1();

		assertTrue(f.isFailed());
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
