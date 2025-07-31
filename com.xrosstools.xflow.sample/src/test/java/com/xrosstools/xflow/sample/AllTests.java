package com.xrosstools.xflow.sample;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.xrosstools.xflow.sample.spring.SpringBeanFactoryTest;
import com.xrosstools.xflow.sample.spring.SpringDeclareTest;

@RunWith(Suite.class)
@SuiteClasses({
        NormalTest.class,
        FailRetryTest.class,
        SuspendResumeTest.class,
        SpecifyRestoreTest.class,
        RouteTest.class,
        SpringBeanFactoryTest.class,
        SpringDeclareTest.class,
})
public class AllTests {

}
