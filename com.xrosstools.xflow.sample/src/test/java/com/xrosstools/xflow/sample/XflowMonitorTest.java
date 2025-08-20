package com.xrosstools.xflow.sample;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.xrosstools.xflow.Xflow;
import com.xrosstools.xflow.XflowContext;
import com.xrosstools.xflow.XflowMonitor;

public class XflowMonitorTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            XflowMonitor monitor = new XflowMonitor(1000);
            monitor.setVisible(true);
            
            Xflow f = UnitTest.WaitActivity.create();
            f.setInstanceId("abc");
    		XflowContext context = new XflowContext();
    		context.put("para1", "abc");
    		context.put("para2", 123);
    		context.put("para3", 23.456);
    		f.start(context);
    		monitor.addInstance(f);
    		
    		f = UnitTest.TaskActivity.create();
            f.setInstanceId("123");
    		context = new XflowContext();
    		context.put("para4", "abc");
    		context.put("para5", 123);
    		context.put("para6", 23.456);
    		f.start(context);
    		monitor.addInstance(f);

        });
    }
}
