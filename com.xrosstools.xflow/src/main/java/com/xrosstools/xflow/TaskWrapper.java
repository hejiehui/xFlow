package com.xrosstools.xflow;

public class TaskWrapper implements AutoTask{
	private TaskStatus status;

	@Override
	public boolean execute(XflowContext ctx) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	public TaskStatus getStatus() {
		return status;
	}

}
