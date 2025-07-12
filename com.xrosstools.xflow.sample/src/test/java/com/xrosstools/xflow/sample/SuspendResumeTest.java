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

public class SuspendResumeTest extends TestAdapter {
	private static final long DUR = 5;
	private Exception exception = new NullPointerException();

	@Test
	public void testAutoActivitySuspendInMain() throws Exception {
		String nodeId = AUTO_ACTIVITY_NODE;
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		//Suspend after auto activity, before end
		injectWait(context, DUR);
		f.start(context);
		
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		f.suspend();
		assertTrue(f.isSuspended());

		waitToInactive(f, nodeId);
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		assertEquals(f.getPendingNodeIds().size(), 1);
		assertEquals(END_NODE, f.getPendingNodeIds().get(0));
		
		try {
			f.retry(END_NODE);
			fail();
		}catch(Throwable e) {
			assertTrue(e instanceof IllegalStateException);
		}

		f.resume();
		
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
	}

	@Test
	public void testAutoActivitySuspendInListener() throws Exception {
		//Suspend after start
		String nodeId = AUTO_ACTIVITY_NODE;
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		
		injectSuspend(context, nodeStarted, START_NODE);
		f.start(context);
		
		waitToSuspend(f);
		assertTrue(f.isSuspended());
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		assertEquals(1, f.getPendingNodeIds().size());
		assertEquals(nodeId, f.getPendingNodeIds().get(0));

		injectSuspend(context, nodeStarted, nodeId);
		f.resume();

		waitToSuspend(f);
		assertTrue(f.isSuspended());
		assertEquals(f.getPendingNodeIds().size(), 1);
		assertEquals(END_NODE, f.getPendingNodeIds().get(0));

		f.resume();
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());

	}

	@Test
	public void testAutoActivitySuspendInActivity() throws Exception {
		//Suspend after start
		String nodeId = AUTO_ACTIVITY_NODE;
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		
		injectSuspend(context);
		f.start(context);
		
		waitToSuspend(f);
		assertEquals(f.getPendingNodeIds().size(), 1);
		assertEquals(END_NODE, f.getPendingNodeIds().get(0));

		f.resume();
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());

	}

	@Test
	public void testAutoActivitySuspendInFailedAndRetry() throws Exception {
		//Suspend after start
		String nodeId = AUTO_ACTIVITY_NODE;
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		
		injectException(context, exception);
		f.start(context);
		
		waitToFail(f, nodeId);
		f.suspend();
		assertTrue(f.isSuspended());
		assertTrue(f.isActive(nodeId));
		assertTrue(f.isFailed(nodeId));

		
		restoreNormal(context);
		//Retry failed
		assertRetryFailed(f, nodeId);		
		f.resume();
		
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
		
		injectSuspend(context, nodeStarted, START_NODE);

		f.start(context);

		//Suspend at start
		assertPending(f, nodeId);
		assertRetryFailed(f, nodeId);
		
		//Because suspend order in context, flow will be suspended
		f.resume();
		
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		sleep();
		
		List<Task> tasks = f.getTasks("Jerry");
		assertEquals(5, tasks.size());
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		f.suspend();
		
		assertTrue(f.isSuspended());
		
		FeedbackTask task = (FeedbackTask)tasks.get(0);
		task.setFeedback("NO");

		//submit in suspend will fail
		try {
			f.submit(task);
			fail();
		}catch(IllegalStateException e) {
		}
		
		f.resume();

		//submit retry with same task succeed
		restoreNormal(context);
		f.submit(task);
		assertTrue(f.isActive(nodeId));

		injectSuspend(context);
		
		//submit with activity succeed
		task = (FeedbackTask)tasks.get(1);
		task.setFeedback("OK");
		f.submit(task);
		
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		assertPending(f, END_NODE);
		
		f.resume();
		
		waitToEnd(f);
		assertTrue(f.isEnded());
		assertTrue(f.getTasks("Jerry").isEmpty());
	}
	
	@Test
	public void testEventActivity() throws Exception {
		String nodeId = EVENT_ACTIVITY_NODE;
		Xflow f = UnitTest.EventActivity.create();
		XflowContext context = new XflowContext();

		injectSuspend(context, nodeStarted, START_NODE);

		f.start(context);

		assertPending(f, nodeId);
		
		assertRetryFailed(f, nodeId);
		
		f.resume();
		
		waitToActive(f, nodeId);

		f.suspend();
		assertTrue(f.isSuspended());
		sleep();
		
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		List<EventSpec> specs = f.getEventSpecs();
		assertEquals(1, specs.size());
		
		//notify fail
		Event event = specs.get(0).create();
		
		try {
			f.notify(event);
			fail();
		}catch(IllegalStateException e) {
		}
		
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		//notify retry succeed
		f.resume();
		restoreNormal(context);
		event = specs.get(0).create();
		f.notify(event);

		waitToEnd(f);
		assertTrue(f.isEnded());
	}

	@Test
	public void testWaitActivity() throws Exception {
		String nodeId = WAIT_ACTIVITY_NODE;
		Xflow f = UnitTest.WaitActivity.create();
		XflowContext context = new XflowContext();
		
		injectSuspend(context, nodeStarted, START_NODE);
		
		long dur = System.currentTimeMillis();
		f.start(context);
		
		assertPending(f, nodeId);

		assertTrue(f.isSuspended());
		
		restoreNormal(context);

		f.resume();
		
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		
		f.suspend();

		assertRetryFailed(f, nodeId);
		
		assertPending(f, END_NODE);
		
		f.resume();

		waitToEnd(f);
		dur = System.currentTimeMillis() - dur;
		System.out.println("dur: " + dur);
		assertTrue(dur > 100 && dur < 115);
	}

	@Test
	public void testBinaryRouterTrue() throws Exception {
		String nodeId = BINARY_ROUTER_NODE;
		Xflow f = UnitTest.BinaryRouter.create();
		
		//True path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, true);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		
		suspendAndResume(f, context, nodeId, 1);

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

		suspendAndResume(f, context, nodeId, 1);
		
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
	
			suspendAndResume(f, context, nodeId, 1);
			
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
		for(String pathes: data.keySet()) {
			int value = data.get(pathes);
			Xflow f = UnitTest.InclusiveRouter.create();
			
			XflowContext context = new XflowContext();
			context.put(TestInclusiveRouter.PROP_KEY_PATHES, pathes);
			context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
	
			suspendAndResume(f, context, nodeId, 2);
			
			waitToEnd(f);
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(value, counter.get());
		}
	}
	
	private void suspendAndResume(Xflow f, XflowContext context, String routerId, int count) throws Exception {
		injectSuspend(context, nodeStarted, START_NODE);
		
		f.start(context);

		assertPending(f, routerId);

		assertFalse(f.isActive(routerId));
		assertFalse(f.isFailed(routerId));

		//Retry failed
		try {
			f.retry(routerId);
			fail();
		}catch(IllegalStateException e) {
		}
		
		injectSuspend(context, nodeStarted, routerId);

		f.resume();
		
		waitToSuspend(f);
		while(f.getPendingNodeIds().isEmpty())
			sleep1();
		assertEquals(count, f.getPendingNodeIds().size());
		
		f.resume();
	}
	
	@Test
	public void testParallelRouterTrue() throws Exception {
		String nodeId = PARALLEL_ROUTER_NODE;
		Xflow f = UnitTest.ParallelRouter.create();
		
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		suspendAndResume(f, context, nodeId, 3);
		
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(10+10+20+30, counter.get());
	}
	
	@Test
	public void testSubflowActivity() throws Exception {
		String nodeId = SUBFLOW_ACTIVITY_NODE;
		String subflowNodeId = SUBFLOW_AUTO_ACTIVITY_ID_1;
		
		Xflow f = UnitTest.SubflowActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestSubflowActivity.COUNT, 10);
		
		//Suspend main node
		injectSuspend(context, nodeStarted, START_NODE);
		f.start(context);
		
		assertPending(f, nodeId);

		assertRetryFailed(f, nodeId);
		
		restoreNormal(context);

		//Inject into subflow
		context.put(SUB_FLOW_SUSPEND, "");
		f.resume();

		//Subflow failed
		sleep1();
		Xflow subflow = f.getSubflow(nodeId);
		assertEquals(SUBFLOW_ID, subflow.getId());
		assertTrue(subflow.isSuspended());

		assertPending(subflow, PARALLEL_ROUTER_NODE);
		
		XflowContext subflowContext = f.getSubflowContext(nodeId);
		
		//For parallel node, it can only be injected like this
		injectSuspend(subflowContext, nodeStarted, PARALLEL_ROUTER_NODE);

		subflow.resume();
		
		sleep1();
		sleep1();
		
		List<String> pendingNodes = subflow.getPendingNodeIds();
		
		assertEquals(3, pendingNodes.size());
		assertTrue(pendingNodes.contains(SUBFLOW_AUTO_ACTIVITY_ID_1));
		assertTrue(pendingNodes.contains(SUBFLOW_AUTO_ACTIVITY_ID_2));
		assertTrue(pendingNodes.contains(SUBFLOW_AUTO_ACTIVITY_ID_3));
		
		assertRetryFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_1);
		assertRetryFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_2);
		assertRetryFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_3);

		//Suspend parent so the merge from subflow will fail
		f.suspend();
		subflow.resume();

		waitToFail(subflow, END_NODE);
		assertTrue(subflow.isActive(END_NODE));
		assertTrue(subflow.isFailed(END_NODE));
		assertTrue(subflow.getFailure(END_NODE) instanceof IllegalStateException);

		f.resume();
		
		subflow.retry(END_NODE);
		
		waitToEnd(subflow);
		
		waitToEnd(f);
		int counter = context.get(TestSubflowActivity.COUNT);
		assertEquals(10+10+20+30, counter);
	}
	
//	private void assertInactive(Xflow f, String nodeId) throws Exception {
//		assertFalse(f.isActive(nodeId));
//		assertFalse(f.isFailed(nodeId));
//	}
	
	private void assertPending(Xflow f, String nodeId) throws Exception {
		waitToSuspend(f);
		while(f.getPendingNodeIds().isEmpty())
			sleep1();
		assertEquals(f.getPendingNodeIds().size(), 1);
		assertEquals(nodeId, f.getPendingNodeIds().get(0));
	}
	
	private void assertRetryFailed(Xflow f, String nodeId) {
		try {
			f.retry(nodeId);
			fail();
		}catch(Throwable e) {
			assertTrue(e instanceof IllegalStateException);
		}
	}
}
