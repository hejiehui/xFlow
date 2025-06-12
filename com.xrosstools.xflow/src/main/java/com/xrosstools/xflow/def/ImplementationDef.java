package com.xrosstools.xflow.def;

import com.xrosstools.xflow.XflowSpring;

//TODO move to common
public class ImplementationDef<T> implements ElementConstants {
	private static boolean enableSpring;
	
	static {
		try {
			Class.forName("org.springframework.context.ApplicationContext");
			enableSpring = true;
		} catch (ClassNotFoundException e) {
			enableSpring = false;
		}
	}

	private String implName;

	public ImplementationDef(String implName) {
		this.implName = implName;
	}
	
	@SuppressWarnings("unchecked")
	public T create() {
		return (T)create(implName);
	}

	@SuppressWarnings("unchecked")
	public T create(Class<? extends MethodWrapper> wrapperClass) {
		if(implName == null || implName.trim().length() == 0)
			throw new IllegalArgumentException("class name is empty.");
		
		String className = implName;
		String methodName = null;
		if(implName.contains(SEPARATOR)) {
			String[] parts = implName.split(SEPARATOR);
			className = parts[0];
			methodName = parts[1];
		}
		
		try {
			Object instance = create(className);
			
			if(methodName == null)
				return (T)instance;

			MethodWrapper wrapper = wrapperClass.getDeclaredConstructor().newInstance();
			wrapper.instance = instance;
			Class<?> clazz = Class.forName(className);
			wrapper.method = clazz.getDeclaredMethod(methodName, wrapper.parameterClasses);
			
			//Allow for invoke private method
			wrapper.method.setAccessible(true);  
			return (T)wrapper;
		} catch(Throwable e) {
			throw new IllegalArgumentException(implName, e);
		}			
	}

	private Object create(String className) {
		if(className == null || className.trim().length() == 0)
			throw new IllegalArgumentException("class name is empty.");
		
		try {
			Class<?> clazz = Class.forName(className);

			Object instance = null;
			if(enableSpring)
				instance = XflowSpring.getBean(className);
			
			if(instance == null)
				instance = clazz.getDeclaredConstructor().newInstance();
			
			return instance;
		} catch(Throwable e) {
			throw new IllegalArgumentException(implName, e);
		}			
	}
}