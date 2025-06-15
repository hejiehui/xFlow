package com.xrosstools.xflow.idea.editor.model;

import java.util.concurrent.TimeUnit;

public interface PropertyConstants {
    int DEFAULT_HEIGHT = 50;
    int DEFAULT_WORKFLOW_HEIGHT = 600;
    int DEFAULT_WORKFLOW_WIDTH = 900;
    int INCREMENTAL = 100;
    int TITLE_SPACE = 10;


    String PROP_ID = "Id";
    String PROP_LABEL = "Label";
    String PROP_DESCRIPTION = "Description";
    String PROP_EVALUATOR = "Evaluator";
    String PROP_IMPLEMENTATION = "Implementation";
    String PROP_DEFAULT_LINK = "Default link";
    String PROP_TRUE_LINK = "True link";
    String PROP_STYLE = "Style";

    String PROP_DELAY = "Delay";
    String PROP_TIME_UNIT = "Time unit";

    String PROP_SUBFLOW = "Subflow";

    String PROPERTIES_CATEGORY = "Properties";

    String PROPERTY_KEY_PREFIX = "PROP_KEY";

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
}
