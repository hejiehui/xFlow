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
    
    public static class MergeEarly {
        public static Xflow create() {
            return load().create("merge early");
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
