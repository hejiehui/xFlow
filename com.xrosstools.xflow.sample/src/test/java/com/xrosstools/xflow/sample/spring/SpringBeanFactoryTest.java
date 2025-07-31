package com.xrosstools.xflow.sample.spring;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowSpring;
import com.xrosstools.xflow.sample.TestAdapter;

@Configuration
@ComponentScan
public class SpringBeanFactoryTest extends TestAdapter {

    @Bean
    XflowSpring createFactory() {
    	return new XflowSpring();
    }
    
    @BeforeClass
    public static void setup() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringBeanFactoryTest.class);
    }

	@Test
	public void testAutoActivity() throws Exception {
		String key = new TestActivityConfig().getKey();
		
		Xflow f = SpringTest.AutoActivity.create();
		XflowContext context = new XflowContext();
		context.put(key, 10);
		f.start(context);
		waitToEnd(f);
		int counter = context.get(key);
		assertEquals(20, counter);
	}
}
