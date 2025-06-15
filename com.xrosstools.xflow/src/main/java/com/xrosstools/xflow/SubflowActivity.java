package com.xrosstools.xflow;

public interface SubflowActivity {
	XflowContext createContext(XflowContext parentContext);
	void mergeContext(XflowContext parentContext, XflowContext subFlowContext);
}
