package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;

/**
    IMPORTANT NOTE!
    This is generated code based on Xross Flow model file "xflow/routes.xflow".
    In case the model file changes, regenerate this file.
    Do Not Modify It.

    

    Last generated time:
    2025-07-16T23:27:30.589+08:00[Asia/Shanghai]
*/
public class Routes {
    
    public static class ParallelCase1 {
        public static Xflow create() {
            return load().create("parallel case1");
        }
    }

    public static class ParallelCase2 {
        public static Xflow create() {
            return load().create("parallel case2");
        }
    }

    public static class InclusiveCase1 {
        public static Xflow create() {
            return load().create("inclusive case1");
        }
    }

    public static class ParallelCase3 {
        public static Xflow create() {
            return load().create("parallel case3");
        }
    }
    

    public static class ParallelCase4 {
        public static Xflow create() {
            return load().create("parallel case4");
        }
    }
    
    public static class InclusiveCase2 {
        public static Xflow create() {
            return load().create("inclusive case2");
        }
    }

    private static volatile XflowFactory factory;
    private static XflowFactory load()  {
        if(factory == null) {
            synchronized(Routes.class) {
                if(factory == null)
                    factory = XflowFactory.load("routes.xflow");
            }
        }
        return factory;
    }
}
