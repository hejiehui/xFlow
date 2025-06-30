package com.xrosstools.xflow;

public enum XflowStatus {
	CREATED(){
		public XflowStatus changeTo(XflowStatus next) {
			return next;
		}
	},

	RUNNING(){
		public XflowStatus changeTo(XflowStatus next) {
			if(next == CREATED)
				illegalState(next);
			return next;
		}
	},

	SUSPENDED(){
		public XflowStatus changeTo(XflowStatus next) {
			if(next == CREATED)
				illegalState(next);
			return next;
		}
	},

	SUCCEED,

	FAILED,

	ABORTED;
	
	public XflowStatus changeTo(XflowStatus next) {
		return illegalState(next);
	}
	
	XflowStatus illegalState(XflowStatus next) {
		throw new IllegalStateException(String.format("Change from %s to %s is illegal", this, next));
	}
}
