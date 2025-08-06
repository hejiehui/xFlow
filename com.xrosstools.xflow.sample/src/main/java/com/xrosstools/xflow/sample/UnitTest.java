package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;

/**
    IMPORTANT NOTE!
    This is generated code based on Xross Flow model file "xflow/unit_test.xflow".
    In case the model file changes, regenerate this file.
    Do Not Modify It.

    

    Last generated time:
    2025-06-09T16:39:44.269+08:00[Asia/Shanghai]
*/
public class UnitTest {
    
    public static class AutoActivity {
        public static Xflow create() {
            return load().create("auto activity");
        }
    }

    public static class TaskActivity {
        public static Xflow create() {
            return load().create("task activity");
        }
    }

    public static class EventActivity {
        public static Xflow create() {
            return load().create("event activity");
        }
    }

    public static class WaitActivity {
        public static Xflow create() {
            return load().create("wait activity");
        }
    }
    
    public static class BinaryRouter {
        public static Xflow create() {
            return load().create("binary router");
        }
    }
    
    public static class ExclusiveRouter {
        public static Xflow create() {
            return load().create("exclusive router");
        }
    }

    public static class InclusiveRouter {
        public static Xflow create() {
            return load().create("inclusive router");
        }
    }

    public static class ParallelRouter {
        public static Xflow create() {
            return load().create("parallel router");
        }
    }

    public static class SubflowActivity {
        public static Xflow create() {
            return load().create("subflow activity");
        }
    }

    public static class FailCase {
        public static Xflow create() {
            return load().create("fail case");
        }
    }

    public static class MethodReference {
        public static Xflow create() {
            return load().create("method reference");
        }
    }
    
    private static volatile XflowFactory factory;
    private static XflowFactory load()  {
        if(factory == null) {
            synchronized(UnitTest.class) {
                if(factory == null)
                    factory = XflowFactory.load("unit_test.xflow");
            }
        }
        return factory;
    }
}
