package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowFactory;

/**
    IMPORTANT NOTE!
    This is generated code based on Xross Flow model file "xflow/routes.xflow".
    In case the model file changes, regenerate this file.
    Do Not Modify It.

    

    Last generated time:
    2025-08-03T14:20:25.236+08:00[Asia/Shanghai]
*/
public class Routes {
    
    public static class ParallelCase1 {
        /*  Node Names */
        public static final String START = "start";

        public static final String END = "end";

        public static final String R1 = "R1";

        public static final String ABC_DEF = "a1";

        public static final String A3 = "a3";

        public static final String A2 = "a2";

        public static final String R2 = "R2";

        public static final String A4 = "a4";

        public static final String R3 = "R3";

        public static Xflow create() {
            return load().create("parallel case1");
        }
    }

    public static class ParallelCase2 {
        /*  Node Names */
        public static final String START = "start";

        public static final String R1 = "R1";

        public static final String R2 = "R2";

        public static final String A1 = "a1";

        public static final String A2 = "a2";

        public static final String A4 = "a4";

        public static final String R3 = "R3";

        public static final String A3 = "a3";

        public static final String A5 = "a5";

        public static final String END = "end";

        public static Xflow create() {
            return load().create("parallel case2");
        }
    }

    public static class ParallelCase3 {
        /*  Node Names */
        public static final String START = "start";

        public static final String R1 = "R1";

        public static final String R6 = "R6";

        public static final String A2 = "2";

        public static final String R2 = "R2";

        public static final String R3 = "R3";

        public static final String A3 = "3";

        public static final String A4 = "4";

        public static final String A5 = "5";

        public static final String A6 = "6";

        public static final String A7 = "7";

        public static final String R5 = "R5";

        public static final String A8 = "8";

        public static final String A1 = "1";

        public static final String END = "end";

        public static final String R4 = "R4";

        public static Xflow create() {
            return load().create("parallel case3");
        }
    }

    public static class ParallelCase4 {
        /*  Node Names */
        public static final String START = "start";

        public static final String R1 = "R1";

        public static final String A1 = "a1";

        public static final String A2 = "a2";

        public static final String R2 = "R2";

        public static final String R5 = "R5";

        public static final String END = "end";

        public static final String R3 = "R3";

        public static final String A3 = "1";

        public static final String A4 = "2";

        public static final String R4 = "R4";

        public static Xflow create() {
            return load().create("parallel case4");
        }
    }

    public static class InclusiveCase1 {
        /*  Node Names */
        public static final String START = "start";

        public static final String END = "end";

        public static final String R1 = "R1";

        public static final String R3 = "R3";

        public static final String A1 = "a1";

        public static final String A2 = "a2";

        public static final String A5 = "a5";

        public static final String R2 = "R2";

        public static final String A4 = "a4";

        public static final String A6 = "a6";

        public static final String A7 = "a7";

        public static final String A3 = "a3";

        public static final String A8 = "a8";

        public static Xflow create() {
            return load().create("inclusive case1");
        }
    }

    public static class InclusiveCase2 {
        /*  Node Names */
        public static final String START = "start";

        public static final String R1 = "R1";

        public static final String A1 = "1";

        public static final String A2 = "2";

        public static final String R2 = "R2";

        public static final String A3 = "3";

        public static final String A4 = "4";

        public static final String R3 = "R3";

        public static final String END = "end";

        public static final String A5 = "5";

        public static Xflow create() {
            return load().create("inclusive case2");
        }
    }

    public static class BinaryCase1 {
        /*  Node Names */
        public static final String START = "start";

        public static final String R1 = "R1";

        public static final String A1 = "a1";

        public static final String R2 = "R2";

        public static final String R5 = "R5";

        public static final String A2 = "a2";

        public static final String A5 = "a5";

        public static final String R3 = "R3";

        public static final String R4 = "R4";

        public static final String A3 = "a3";

        public static final String A6 = "a6";

        public static final String A8 = "a8";

        public static final String R7 = "R7";

        public static final String A7 = "a7";

        public static final String R6 = "R6";

        public static final String R8 = "R8";

        public static final String END = "end";

        public static final String A9 = "a9";

        public static final String A4 = "a4";

        public static final String A11 = "a11";

        public static final String A10 = "a10";

        public static Xflow create() {
            return load().create("binary case1");
        }
    }

    public static class ExclusiveCase1 {
        /*  Node Names */
        public static final String START = "start";

        public static final String END = "end";

        public static final String R1 = "R1";

        public static final String AX1 = "1";

        public static final String A2 = "2";

        public static final String A4 = "4";

        public static final String R2 = "R2";

        public static final String R3 = "R3";

        public static final String A5 = "5";

        public static final String A7 = "7";

        public static final String A6 = "6";

        public static final String R4 = "R4";

        public static final String A11 = "11";

        public static final String R5 = "R5";

        public static final String A3 = "3";

        public static final String R6 = "R6";

        public static final String A10 = "10";

        public static final String R8 = "R8";

        public static final String A12 = "12";

        public static final String A13 = "13";

        public static final String R9 = "R9";

        public static final String R10 = "R10";

        public static final String A14 = "14";

        public static final String A9 = "9";

        public static final String R7 = "R7";

        public static final String A1 = "a1";

        public static Xflow create() {
            return load().create("exclusive case1");
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
