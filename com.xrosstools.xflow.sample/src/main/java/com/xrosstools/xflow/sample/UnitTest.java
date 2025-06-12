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
