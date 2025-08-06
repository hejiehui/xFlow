package com.xrosstools.xflow.sample;

import com.xrosstools.xflow.*;

import java.util.concurrent.atomic.AtomicInteger;

public class TestMethod implements GlobalConfigAware, FlowConfigAware, NodeConfigAware {
    public static final String PROP_KEY_STEP = "step";

    public static final String KEY_COUNTER = "counter";
    public static final String KEY_PATH = "path";
    public static final String KEY_PATHES = "pathes";
    public static final String KEY_RESULT = "result";

    private int step;

    private DataMap flowConfig;
    private DataMap globalConfig;

    private void copy(XflowContext context) {
        context.copyFrom(flowConfig, "flowString");
        context.copyFrom(globalConfig, "globalA");
    }

    public void autoActivityExecute(XflowContext context) {
        copy(context);
        AtomicInteger counter = context.get(KEY_COUNTER);
        counter.addAndGet(step);
    }
    public boolean binaryRoute(XflowContext context) {
        copy(context);
        return context.get(KEY_RESULT);
    }

    public String exclusiveRoute(XflowContext context) {
        copy(context);
        return context.get(KEY_PATH);
    }

    public String[] inclusiveRoute(XflowContext context) {
        copy(context);
        String pathes = context.get(KEY_PATHES);
        return pathes.split(",");
    }

    @Override
    public void initFlowConfig(DataMap config) {
        flowConfig = config;
    }

    @Override
    public void initGlobalConfig(DataMap config) {
        globalConfig = config;
    }

    @Override
    public void initNodeConfig(DataMap config) {
        if(config.contains(PROP_KEY_STEP))
            step = config.get(PROP_KEY_STEP);
    }
}
