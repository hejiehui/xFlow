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
import com.xrosstools.xflow.XflowRecorder;

public class SaveRestoreTest extends TestAdapter {
	private static final long DUR = 5;
	private Exception exception = new NullPointerException();

	@Test
	public void testSaveResumeAtCREATE() throws Exception {
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		try {
			f.specify();
			fail();
		}catch(Throwable e) {
			assertTrue(e instanceof IllegalStateException);
		}
	}
	
	@Test
	public void testSaveResumeAtRUNNING() throws Exception {
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		injectWait(context, DUR);
		f.start(context);
		try {
			f.specify();
			fail();
		}catch(Throwable e) {
			assertTrue(e instanceof IllegalStateException);
		}
	}
	
	@Test
	public void testAutoActivity() throws Exception {
		String nodeId = AUTO_ACTIVITY_NODE;
		Xflow f = UnitTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		injectSuspend(context, nodeStarted, START_NODE);
		f.start(context);

		waitToSuspend(f);
		XflowRecorder recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());
		
		//Create new instance and restore
		f = UnitTest.AutoActivity.create();
		
		//Suspend after current activity
		injectSuspend(context);
		f.restore(context, recorder);
		
		assertPending(f, END_NODE);

		recorder = f.specify();

		f = UnitTest.AutoActivity.create();
		restoreNormal(context);
		f.restore(context, recorder);

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
		XflowRecorder recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.TaskActivity.create();
		f.restore(context, recorder);
		
		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		sleep();
		
		List<Task> tasks = f.getTasks("Jerry");
		assertEquals(5, tasks.size());
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		f.suspend();
		
		recorder = f.specify();
		
		assertTrue(recorder.getEventSpecs().isEmpty());
		assertEquals(5, recorder.getTasks().size());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.TaskActivity.create();
		f.restore(context, recorder);

		FeedbackTask task = (FeedbackTask)tasks.get(0);
		task.setFeedback("NO");

		f.submit(task);
		assertTrue(f.isActive(nodeId));

		f.suspend();
		recorder = f.specify();
		
		assertTrue(recorder.getEventSpecs().isEmpty());
		assertEquals(5, recorder.getTasks().size());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.TaskActivity.create();
		f.restore(context, recorder);

		injectSuspend(context);
		//submit with activity succeed
		task = (FeedbackTask)tasks.get(1);
		task.setFeedback("OK");
		f.submit(task);
		
		assertFalse(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));
		
		assertPending(f, END_NODE);
		
		recorder = f.specify();
		
		assertTrue(recorder.getEventSpecs().isEmpty());
		assertEquals(0, recorder.getTasks().size());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.TaskActivity.create();
		f.restore(context, recorder);
		
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

		waitToSuspend(f);
		
		XflowRecorder recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.EventActivity.create();
		f.restore(context, recorder);

		waitToActive(f, nodeId);

		f.suspend();
		assertTrue(f.isSuspended());
		sleep();
		
		assertTrue(f.isActive(nodeId));
		assertFalse(f.isFailed(nodeId));

		List<EventSpec> specs = f.getEventSpecs();
		assertEquals(1, specs.size());
		
		recorder = f.specify();

		assertEquals(1, recorder.getEventSpecs().size());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.EventActivity.create();
		f.restore(context, recorder);
		
		specs = f.getEventSpecs();
		assertEquals(1, specs.size());
		
		Event event = specs.get(0).create();
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
		
		XflowRecorder recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.WaitActivity.create();
		f.restore(context, recorder);

		waitToActive(f, nodeId);
		assertTrue(f.isActive(nodeId));

		//Fail when wait node is running
		try {
			f.specify();
			fail();
		}catch(IllegalStateException e) {
		}

		f.suspend();

		try {
			f.specify();
			fail();
		}catch(IllegalStateException e) {
		}

		assertPending(f, END_NODE);
		
		recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = UnitTest.WaitActivity.create();
		f.restore(context, recorder);

		waitToEnd(f);
		dur = System.currentTimeMillis() - dur;
		System.out.println("dur: " + dur);
		assertTrue(dur > 100 && dur < 120);
	}

	@Test
	public void testBinaryRouterTrue() throws Exception {
		String nodeId = BINARY_ROUTER_NODE;
		Xflow f = UnitTest.BinaryRouter.create();
		Xflow f1 = UnitTest.BinaryRouter.create();
		Xflow f2 = UnitTest.BinaryRouter.create();
		
		//True path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, true);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
		
		suspendAndRestore(f, context, nodeId, 1, f1, f2);

		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(20, counter.get());
	}

	@Test
	public void testBinaryRouterFalse() throws Exception {
		String nodeId = BINARY_ROUTER_NODE;
		Xflow f = UnitTest.BinaryRouter.create();
		Xflow f1 = UnitTest.BinaryRouter.create();
		Xflow f2 = UnitTest.BinaryRouter.create();

		//False path
		XflowContext context = new XflowContext();
		context.put(TestBinaryRouter.PROP_KEY_RESULT, false);
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		suspendAndRestore(f, context, nodeId, 1, f1, f2);
		
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(30, counter.get());
	}

	@Test
	public void testExclusiveRouterTrue() throws Exception {
		String nodeId = INCLUSIVE_ROUTER_NODE;
		for(int i = 1; i <= 3; i++) {
			Xflow f = UnitTest.ExclusiveRouter.create();
			Xflow f1 = UnitTest.ExclusiveRouter.create();
			Xflow f2 = UnitTest.ExclusiveRouter.create();
			
			XflowContext context = new XflowContext();
			//p1, p2, p3
			context.put(TestExclusiveRouter.PROP_KEY_PATH, "p" + i);
			context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
	
			suspendAndRestore(f, context, nodeId, 1, f1, f2);
			
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
			Xflow f1 = UnitTest.InclusiveRouter.create();
			Xflow f2 = UnitTest.InclusiveRouter.create();
			
			XflowContext context = new XflowContext();
			context.put(TestInclusiveRouter.PROP_KEY_PATHES, pathes);
			context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));
	
			suspendAndRestore(f, context, nodeId, 2, f1, f2);
			
			waitToEnd(f);
			AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
			assertEquals(value, counter.get());
		}
	}
	
	private void suspendAndRestore(Xflow f, XflowContext context, String routerId, int count, Xflow f1, Xflow f2) throws Exception {
		injectSuspend(context, nodeStarted, START_NODE);
		
		f.start(context);

		assertPending(f, routerId);

		assertFalse(f.isActive(routerId));
		assertFalse(f.isFailed(routerId));

		XflowRecorder recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(1, recorder.getTokenRecorders().size());		

		f = f1;

		injectSuspend(context, nodeStarted, routerId);
		f.restore(context, recorder);
		
		waitToSuspend(f);
		while(f.getPendingNodeIds().isEmpty())
			sleep1();
		assertEquals(count, f.getPendingNodeIds().size());
		
		recorder = f.specify();

		assertTrue(recorder.getEventSpecs().isEmpty());
		assertTrue(recorder.getTasks().isEmpty());
		assertEquals(count, recorder.getTokenRecorders().size());		

		f = f2;
		f.restore(context, recorder);
	}
	
	@Test
	public void testParallelRouterTrue() throws Exception {
		String nodeId = PARALLEL_ROUTER_NODE;
		Xflow f = UnitTest.ParallelRouter.create();
		Xflow f1 = UnitTest.ParallelRouter.create();
		Xflow f2 = UnitTest.ParallelRouter.create();
		
		XflowContext context = new XflowContext();
		context.put(TestAutoActivity.PROP_KEY_COUNTER, new AtomicInteger(10));

		suspendAndRestore(f, context, nodeId, 3, f1, f2);
		
		waitToEnd(f);
		AtomicInteger counter = context.get(TestAutoActivity.PROP_KEY_COUNTER);
		assertEquals(10+10+20+30, counter.get());
	}
	
//	@Test
//	public void testSubflowActivity() throws Exception {
//		String nodeId = SUBFLOW_ACTIVITY_NODE;
//		String subflowNodeId = SUBFLOW_AUTO_ACTIVITY_ID_1;
//		
//		Xflow f = UnitTest.SubflowActivity.create();
//		XflowContext context = new XflowContext();
//		context.put(TestSubflowActivity.COUNT, 10);
//		
//		//Suspend main node
//		injectSuspend(context, nodeStarted, START_NODE);
//		f.start(context);
//		
//		assertPending(f, nodeId);
//
//		assertRetryFailed(f, nodeId);
//		
//		restoreNormal(context);
//
//		//Inject into subflow
//		context.put(SUB_FLOW_SUSPEND, "");
//		f.resume();
//
//		//Subflow failed
//		sleep1();
//		Xflow subflow = f.getSubflow(nodeId);
//		assertEquals(SUBFLOW_ID, subflow.getId());
//		assertTrue(subflow.isSuspended());
//
//		assertPending(subflow, PARALLEL_ROUTER_NODE);
//		
//		XflowContext subflowContext = f.getSubflowContext(nodeId);
//		
//		//For parallel node, it can only be injected like this
//		injectSuspend(subflowContext, nodeStarted, PARALLEL_ROUTER_NODE);
//
//		subflow.resume();
//		
//		sleep1();
//		sleep1();
//		
//		List<String> pendingNodes = subflow.getPendingNodeIds();
//		
//		assertEquals(3, pendingNodes.size());
//		assertTrue(pendingNodes.contains(SUBFLOW_AUTO_ACTIVITY_ID_1));
//		assertTrue(pendingNodes.contains(SUBFLOW_AUTO_ACTIVITY_ID_2));
//		assertTrue(pendingNodes.contains(SUBFLOW_AUTO_ACTIVITY_ID_3));
//		
//		assertRetryFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_1);
//		assertRetryFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_2);
//		assertRetryFailed(subflow, SUBFLOW_AUTO_ACTIVITY_ID_3);
//
//		//Suspend parent so the merge from subflow will fail
//		f.suspend();
//		subflow.resume();
//
//		waitToFail(subflow, END_NODE);
//		assertTrue(subflow.isActive(END_NODE));
//		assertTrue(subflow.isFailed(END_NODE));
//		assertTrue(subflow.getFailure(END_NODE) instanceof IllegalStateException);
//
//		f.resume();
//		
//		subflow.retry(END_NODE);
//		
//		waitToEnd(subflow);
//		
//		waitToEnd(f);
//		int counter = context.get(TestSubflowActivity.COUNT);
//		assertEquals(10+10+20+30, counter);
//	}
	
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
