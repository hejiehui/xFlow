package com.xrosstools.xflow;

public interface TaskActivityListener {
	/**
	 * Notify there is a input task created
	 * @param id
	 * @param message
	 */
	void notify(String inputTask, String id, String message);
}
