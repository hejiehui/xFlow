package com.xrosstools.xflow.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

public class FailRetryTest extends TestAdapter {
	/*
	 * All cases are organized in failed, retry failed, retry success manner.
	 * 
	 * Only nodes that require implementation will be tested.
	 * 
	 * That means start, end, parallel and wait node will not be tested
	 */
	
	private Exception exception = new NullPointerException();

	@Test
	public void testAutoActivity() throws Exception {
		String nodeId = AUTO_ACTIVITY_NODE;
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		
		//Failed
		injectException(context, exception);
		f.start(context);
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));
		
		//Retry failed
		try {
			f.retry(nodeId);
			fail();
		}catch(Throwable e) {
			assertEquals(e, exception);
		}
		
		//Retry succeed
		restoreNormal(context);
		f.retry(nodeId);
		
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
	}


	@Test
	public void testTaskActivity() throws Exception {
		String nodeId = TASK_ACTIVITY_NODE;
		Xflow f = UnitTest.TaskActivity.create();
		XflowContext context = new XflowContext();
		
		injectException(context, exception);

		f.start(context);

		//Failed
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		waitToFail(f, nodeId);
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));
		assertEquals(exception, f.getFailure(nodeId));
		
		//Retry failed
		try {
			f.retry(nodeId);
			fail();
		}catch(Throwable e) {
			assertEquals(e, exception);
		}
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		
		//Retry succeed
		restoreNormal(context);
		f.retry(nodeId);
		List<Task> tasks = f.getTasks("Jerry");
		assertEquals(5, tasks.size());
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		FeedbackTask task = (FeedbackTask)tasks.get(0);
		task.setFeedback("NO");

		//submit failed
		injectException(context, exception);

		try {
			f.submit(task);
			fail();
		}catch(NullPointerException e) {
		}
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		assertFalse(f.isEnded());

		//submit retry
		restoreNormal(context);
		f.submit(task);
		assertTrue(f.isActive(nodeId));
		
		//submit retry with same task fail
		try {
			f.submit(task);
			fail();
		}catch(IllegalArgumentException e) {
		}
		
		//submit retry with same task succeed
		restoreNormal(context);
		task.clearSubmitFlag();
		f.submit(task);
		assertTrue(f.isActive(nodeId));
		
		//submit with activity succeed
		task = (FeedbackTask)tasks.get(1);
		task.setFeedback("OK");
		f.submit(task);
		assertFalse(f.isFailed(nodeId));
		
		sleep();
		assertTrue(f.isEnded());
		assertTrue(f.getTasks("Jerry").isEmpty());
	}
	
	@Test
	public void testEventActivity() throws Exception {
		String nodeId = EVENT_ACTIVITY_NODE;
		Xflow f = UnitTest.EventActivity.create();
		XflowContext context = new XflowContext();

		injectException(context, exception);

		f.start(context);

		//specify fail
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		waitToFail(f, nodeId);
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));

		//specify retry
		restoreNormal(context);
		f.retry(nodeId);

		assertTrue(f.isActive(nodeId));
		sleep1();
		assertFalse(f.isFailed(nodeId));
		
		List<EventSpec> specs = f.getEventSpecs();
		assertEquals(1, specs.size());
		
		//notify fail
		injectException(context, exception);
		Event event = specs.get(0).create();
		
		try {
			f.notify(event);
			fail();
		}catch(Throwable e) {
			assertEquals(e, exception);
		}
		
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));

		//notify retry succeed
		restoreNormal(context);
		event = specs.get(0).create();
		f.notify(event);

		waitToEnd(f);
		assertTrue(f.isEnded());
	}

	@Test
	public void testBinaryRouterTrue() throws Exception {
		String nodeId = BINARY_ROUTER_NODE;
		Xflow f = UnitTest.BinaryRouter.create();
		
		//True path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, true);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		failAndRetry(nodeId, f, context);

		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
	}

	@Test
	public void testBinaryRouterFalse() throws Exception {
		String nodeId = BINARY_ROUTER_NODE;
		Xflow f = UnitTest.BinaryRouter.create();

		//False path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, false);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		failAndRetry(nodeId, f, context);
		
		waitToEnd(f);

		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(30, counter.get());
	}

	@Test
	public void testExclusiveRouterTrue() throws Exception {
		String nodeId = INCLUSIVE_ROUTER_NODE;
		for(int i = 1; i <= 3; i++) {
			Xflow f = UnitTest.ExclusiveRouter.create();
			
			XflowContext context = new XflowContext();
			//p1, p2, p3
			context.put(TestExclusiveRouter.PROP_KEY_PATH, "p" + i);
			context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
	
			failAndRetry(nodeId, f, context);
			
			waitToEnd(f);
			
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(10 * (i + 1), counter.get());
		}
	}
	
	@Test
	public void testInclusiveRouterTrue() throws Exception {
		String nodeId = INCLUSIVE_ROUTER_NODE;
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
	
			failAndRetry(nodeId, f, context);
			waitToEnd(f);
	
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(value, counter.get());
		}
	}
	
	private void failAndRetry(String nodeId, Xflow f, XflowContext context) throws Exception {
		//Route failed
		injectException(context, exception);
		f.start(context);
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));

		//Retry failed
		try {
			f.retry(nodeId);
			fail();
		}catch(Throwable e) {
			assertEquals(e, exception);
		}
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));

		//Retry succeed
		restoreNormal(context);
		f.retry(nodeId);
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
	}
	
	@Test
	public void testSubflowActivity() throws Exception {
		String nodeId = SUBFLOW_ACTIVITY_NODE;
		String subflowNodeId = SUBFLOW_AUTO_ACTIVITY_ID_1;
		
		Xflow f = UnitTest.SubflowActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestSubflowActivity.COUNT, 10);
		
		//Create context failed
		injectException(context, exception);
		f.start(context);
		waitToFail(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));

		//Retry failed
		try {
			f.retry(nodeId);
			fail();
		}catch(Throwable e) {
			assertEquals(e, exception);
		}
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));

		//Retry succeed
		restoreNormal(context);
		context.put(TestSubflowActivity.ERROR, exception);
		f.retry(nodeId);
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		//Subflow failed
		Xflow subflow = f.getSubflow(nodeId);
		assertEquals(SUBFLOW_ID, subflow.getId());

		List<String> ids = subflow.getFailedNodeIds();
		
		assertFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_1);
		assertFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_2);
		assertFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_3);
		
		//Subflow retry failed
		try {
			subflow.retry(SUBFLOW_AUTO_ACTIVITY_ID_1);
			subflow.retry(SUBFLOW_AUTO_ACTIVITY_ID_2);
			subflow.retry(SUBFLOW_AUTO_ACTIVITY_ID_3);
			fail();
		}catch(Throwable e) {
			assertTrue(e instanceof NullPointerException);
		}
		assertFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_1);
		assertFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_2);
		assertFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_3);
		
		//Subflow retry succeed, merge failed
		XflowContext subflowContext = f.getSubflowContext(nodeId);
		restoreNormal(subflowContext);
		
		injectException(context, exception);
		
		subflow.retry(SUBFLOW_AUTO_ACTIVITY_ID_1);
		subflow.retry(SUBFLOW_AUTO_ACTIVITY_ID_2);
		subflow.retry(SUBFLOW_AUTO_ACTIVITY_ID_3);

		assertInactive(subflow, SUBFLOW_AUTO_ACTIVITY_ID_1);
		assertInactive(subflow, SUBFLOW_AUTO_ACTIVITY_ID_2);
		assertInactive(subflow, SUBFLOW_AUTO_ACTIVITY_ID_3);

		waitToEnd(subflow);
		
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));
		
		//Remerge failed
		try {
			f.mergeSubflow(nodeId);
			fail();
		}catch(Throwable e) {
			assertEquals(e, exception);
		}
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertEquals(exception, f.getFailure(nodeId));
		
		//Remerge succeed
		restoreNormal(context);
		f.mergeSubflow(nodeId);
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		waitToEnd(f);
		int counter = context.get(TestSubflowActivity.COUNT);
		assertEquals(10+10+20+30, counter);
	}
	
	private void assertFailed(Xflow f, String nodeId) throws Exception {
		waitToFail(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));
		assertTrue(f.getFailure(nodeId) instanceof NullPointerException);
	}
	
	private void assertInactive(Xflow f, String nodeId) throws Exception {
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
	}
}
