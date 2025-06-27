package com.xrosstools.xflow;

public interface SubflowActivity {
	XflowContext createContext(XflowContext parentContext);
	void mergeSubflow(XflowContext parentContext, XflowContext subFlowContext);
}
