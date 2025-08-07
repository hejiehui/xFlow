package com.xrosstools.xflow.sample.spring;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;

/**
    IMPORTANT NOTE!
    This is generated code based on Xross Flow model file "xflow/spring_test.xflow".
    In case the model file changes, regenerate this file.
    Do Not Modify It.

    

    Last generated time:
    2025-08-07T15:42:10.283+08:00[Asia/Shanghai]
*/
public class SpringTest {
    
    //Diagram level user defined properties
    public static final String GLOBAL_VAR_1 = "global var 1";

    public static final String GOBAL_VAR_2 = "gobal var 2";

    public static class AutoActivity {
        //Xflow level user defined properties
        public static final String FLOW_VAR_2 = "flow var 2";

        public static final String FLOW_VAR_1 = "flow var 1";

        /*  Node Names */
        public static final String START = "start";

        public static final String END = "end";

        public static final String BBB = "a1";

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
