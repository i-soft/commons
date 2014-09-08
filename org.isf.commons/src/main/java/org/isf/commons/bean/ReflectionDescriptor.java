package org.isf.commons.bean;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

public interface ReflectionDescriptor<K extends ReflectionDescriptor<K,T>, T extends ReflectionDescriptor<K,T>> extends AnnotatedElement {
	
	public static final String separator = ".";
	
	public String getDisplayName();
	public String getName();
	public String getPath();
	
	public Class<?> getType();	
	public boolean hasGenericTypes();	
	public Class<?>[] getGenericTypes();
	
	public ReflectionDescriptor<K, T> getParent();
	public K getRoot();
	
	public boolean hasChildren();
	public List<T> getChildren();
	public T getChild(String name);
	
	public URI getURI();
	
	public Object invoke(Object bean, String path, boolean force, Object ... params) throws InvocationTargetException, IllegalAccessException, InstantiationException;
	
}
