package com.xrosstools.xflow;

import java.util.List;

public interface NodeHandler {
	List<ActiveToken> handle(ActiveToken token);
}
