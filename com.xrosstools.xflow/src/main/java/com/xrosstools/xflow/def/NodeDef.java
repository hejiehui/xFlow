package com.xrosstools.xflow.def;

import java.util.concurrent.TimeUnit;

import com.xrosstools.xflow.AutoActivity;
import com.xrosstools.xflow.AutoActivityNode;
import com.xrosstools.xflow.BinaryRouter;
import com.xrosstools.xflow.BinaryRouterNode;
import com.xrosstools.xflow.DataMap;
import com.xrosstools.xflow.EndNode;
import com.xrosstools.xflow.EventActivity;
import com.xrosstools.xflow.EventActivityNode;
import com.xrosstools.xflow.ExclusiveRouter;
import com.xrosstools.xflow.ExclusiveRouterNode;
import com.xrosstools.xflow.InclusiveRouter;
import com.xrosstools.xflow.InclusiveRouterNode;
import com.xrosstools.xflow.Node;
import com.xrosstools.xflow.ParallelRouterNode;
import com.xrosstools.xflow.StartNode;
import com.xrosstools.xflow.TaskActivity;
import com.xrosstools.xflow.TaskActivityNode;
import com.xrosstools.xflow.WaitActivityNode;
import com.xrosstools.xflow.def.MethodWrapper.ActivityMethodWrapper;
import com.xrosstools.xflow.def.MethodWrapper.BinaryRouteMethodWrapper;
import com.xrosstools.xflow.def.MethodWrapper.ExclusiveRouteMethodWrapper;
import com.xrosstools.xflow.def.MethodWrapper.InclusiveRouteMethodWrapper;

public abstract class NodeDef {
	private DataMap config;
	
	public void setConfig(DataMap config) {
		this.config = config;
	}

	public DataMap getConfig() {
		return config.copy();
	}

	public abstract Node create();
	
	public static NodeDef activityNodeDef(final String name, final ImplementationDef<AutoActivity> implDef) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new AutoActivityNode(name, implDef.create(ActivityMethodWrapper.class));
			}
		};
	}
	
	public static NodeDef taskActivityNodeDef(final String name, final ImplementationDef<TaskActivity> implDef) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new TaskActivityNode(name, implDef.create());
			}
		};
	}
	
	public static NodeDef eventActivityNodeDef(final String name, final ImplementationDef<EventActivity> implDef) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new EventActivityNode(name, implDef.create());
			}
		};
	}
	
	public static NodeDef waitActivityNodeDef(final String name, final int delay, final TimeUnit unit) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new WaitActivityNode(name, delay, unit);
			}
		};
	}
	
	public static NodeDef startNodeDef(final String name) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new StartNode(name);
			}
		};
	}
	
	public static NodeDef endNodeDef(final String name) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new EndNode(name);
			}
		};
	}
	
	public static NodeDef binaryRouteNodeDef(final String name, final ImplementationDef<BinaryRouter> implDef) {
		return new NodeDef() {
			@Override
			public Node create() {
				return implDef == null ? new BinaryRouterNode(name) : new BinaryRouterNode(name, implDef.create(BinaryRouteMethodWrapper.class));
			}
		};
	}
	
	public static NodeDef inclusiveRouteNodeDef(final String name, final ImplementationDef<InclusiveRouter> implDef) {
		return new NodeDef() {
			@Override
			public Node create() {
				return implDef == null ? new InclusiveRouterNode(name) : new InclusiveRouterNode(name, implDef.create(InclusiveRouteMethodWrapper.class));
			}
		};
	}
	
	public static NodeDef exclusiveRouteNodeDef(final String name, final ImplementationDef<ExclusiveRouter> implDef) {
		return new NodeDef() {
			@Override
			public Node create() {
				return implDef == null ? new ExclusiveRouterNode(name) : new ExclusiveRouterNode(name, implDef.create(ExclusiveRouteMethodWrapper.class));
			}
		};
	}
	
	public static NodeDef paralleRouterNodeDef(final String name) {
		return new NodeDef() {
			@Override
			public Node create() {
				return new ParallelRouterNode(name);
			}
		};
	}
}
