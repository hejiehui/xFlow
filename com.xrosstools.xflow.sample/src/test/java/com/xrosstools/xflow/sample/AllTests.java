package com.xrosstools.xflow.sample;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        NormalTest.class,
        FailRetryTest.class,
        SuspendResumeTest.class,
})
public class AllTests {

}
