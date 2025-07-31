package com.xrosstools.xflow.sample.spring;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;

/**
    IMPORTANT NOTE!
    This is generated code based on Xross Flow model file "spring_test.xflow".
    In case the model file changes, regenerate this file.
    Do Not Modify It.

    

    Last generated time:
    2025-07-31T15:21:12.665+08:00[Asia/Shanghai]
*/
public class SpringTest {
    
    public static class AutoActivity {
        public static Xflow create() {
            return load().create("auto activity");
        }
    }


    private static volatile XflowFactory factory;
    private static XflowFactory load()  {
        if(factory == null) {
            synchronized(SpringTest.class) {
                if(factory == null)
                    factory = XflowFactory.load("spring_test.xflow");
            }
        }
        return factory;
    }
}
