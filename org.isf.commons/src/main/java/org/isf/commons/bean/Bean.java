package org.isf.commons.bean;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.isf.commons.Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class Bean implements ReflectionDescriptor<Bean, Attribute> {

	private Class<?> beanClass;
	private List<Attribute> children = new ArrayList<Attribute>();
	
	public Bean(Class<?> beanClass) {
		setBeanClass(beanClass);
	}
	
	public Bean(Object obj) {
		this(obj.getClass());
	}
	
	public Bean(String beanClass) throws ClassNotFoundException {
		this(Class.forName(beanClass));
	}
	
	public Class<?> getBeanClass() { return beanClass; }
	protected void setBeanClass(Class<?> beanClass) {
		try {
			beanClass.getConstructor();
		} catch(NoSuchMethodException nsme) {
			throw new IllegalArgumentException("Class: "+beanClass.getName()+" has no standard-constructor.");
		}
		this.beanClass = beanClass; 
		getChildren().addAll(initialMethods(beanClass, this));
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return beanClass.getAnnotation(annotationClass);
	}

	@Override
	public Annotation[] getAnnotations() {
		return getBeanClass().getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getBeanClass().getDeclaredAnnotations();
	}

	@Override
	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return getBeanClass().isAnnotationPresent(annotationClass);
	}

	@Override
	public String getDisplayName() {
		return getBeanClass().getSimpleName();
	}

	@Override
	public String getName() {
		return getBeanClass().getName();
	}

	@Override
	public String getPath() {
		return "";
	}
	
	public Class<?> getType() {
		return getBeanClass();
	}
	
	public boolean hasGenericTypes() {
		return getGenericTypes() != null && getGenericTypes().length > 0;
	}
	
	public Class<?>[] getGenericTypes() {
		java.lang.reflect.Type stype = getType().getGenericSuperclass();
		if (stype instanceof ParameterizedType) {
			List<Class<?>> l = Bean.getGenerics((ParameterizedType)stype);
			return l.toArray(new Class<?>[0]);
		}
		return new Class<?>[0];
	}

	@Override
	public ReflectionDescriptor<Bean, Attribute> getParent() { return null; }

	@Override
	public Bean getRoot() { return this; }
	
	@Override
	public URI getURI() {
		try {
			return new URI("bean", getName(), null, null);
		} catch(URISyntaxException use) {
			return null;
		}
	}

	@Override
	public boolean hasChildren() { return getChildren().size() > 0; }
	
	@Override
	public List<Attribute> getChildren() { return children; }
	
	public List<Attribute> getChildrenAnnotatedWith(Class<? extends Annotation> annotationClass) {
		List<Attribute> l = new ArrayList<Attribute>();
		for (Attribute attr : getChildren())
			if (attr.isAnnotationPresent(annotationClass))
				l.add(attr);
		return l;
	}

	@Override
	public Attribute getChild(String name) {
		for (Attribute a : getChildren())
			if (name.equals(a.getName())) return a;
		return null;
	}

	protected Attribute getChildPerPathRecursive(Attribute attr, String path) {
		path = Util.cutFirstSegment(path, separator);
		String nextsegment = Util.firstSegment(path, separator);
		if (path != null && path.length() > 0) {
			Attribute sattr = attr.getChild(SegmentEncoder.getRealSegment(nextsegment));
			if (sattr != null) return getChildPerPathRecursive(sattr, path);
		}
		return attr;
	}
	
	public Attribute getChildPerPath(String path) {
		if (separator.equals(path)) return null;
		if (path.startsWith(File.separator)) path = path.substring(1);
		Attribute attr = getChild(SegmentEncoder.getRealSegment(Util.firstSegment(path, separator)));
		if (attr == null) return null;
		else return getChildPerPathRecursive(attr, path);
	}
	
	public Object invoke(Object bean, String path, boolean force, Object ... params) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		if (separator.equals(path)) return bean;
		if (path.startsWith(File.separator)) path = path.substring(1);
		Attribute attr = getChild(SegmentEncoder.getRealSegment(Util.firstSegment(path, separator)));
		if (attr == null) 
			if (!force) throw new IllegalArgumentException("Bean '"+getName()+"' attribute '"+Util.firstSegment(path, separator)+"' unknown");
			else return null;
		return attr.invoke(bean, path, force, params);
	}
	
	private void printAttribute(Attribute a, String prefix, int currentHops, int hops) {
		if (currentHops > hops) return;
		System.out.println(prefix+a.getName()+"["+a.getDataType()+"] ("+a.getURI()+")");
		for (Attribute ac : a.getChildren())
			printAttribute(ac, prefix+"  ", currentHops++, hops);
	}
	
	public void print(int hops) {
		System.out.println("bean: "+getName()+" ("+getURI()+")");
		for (Attribute a : getChildren())
			printAttribute(a, "  ", 1, hops);
	}
	
	public void print() {
		print(3);
	}

	/* static methods */
	public static boolean isBeanMethod(Method m) {
		return ((m.getName().startsWith("get") || m.getName().startsWith("is")) && m.getParameterTypes().length == 0) 
				|| (m.getName().startsWith("set") && m.getParameterTypes().length == 1) 
				&& !Modifier.isFinal(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers()) && !Modifier.isProtected(m.getModifiers()) && !Modifier.isStatic(m.getModifiers()) && !Modifier.isTransient(m.getModifiers());
	}
	
	public static Method[] listBeanSetterMethods(Class<?> beanClass) {
		List<Method> l = new ArrayList<Method>();
		for (Method m : beanClass.getMethods())
			if (m.getName().startsWith("set") && isBeanMethod(m))
				l.add(m);
		return l.toArray(new Method[0]);
	}
	
	public static List<Attribute> initialMethods(Class<?> beanClass, ReflectionDescriptor<Bean, Attribute> parent) {
		List<Attribute> ret = new ArrayList<Attribute>();
		Multimap<String, Method> mmap = ArrayListMultimap.create();
		for (Method m : beanClass.getMethods()) {
			if (isBeanMethod(m))
				mmap.put(Util.normalizeName(m.getName()), m);
		}
		
		for (String key : mmap.keySet()) {
			if ("class".equals(key)) continue;
//			System.out.println(key);
			Method getter = null;
			Method setter = null;
			for (Method m : mmap.get(key)) {
				if (m.getName().startsWith("set")) setter = m;
				else getter = m;
			}
			if (getter == null) continue;
			ret.add(new Attribute(parent, getter, setter));
		}
		return ret;
	}

//	public static Object invokeBean(Object bean, URI uri, Object ... params) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
//		return invokeBean(bean, uri, false, params);
//	}
	
	public static Object invokeBean(Object bean, URI uri, boolean force, Object ... params) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
		if (bean == null) throw new IllegalArgumentException("bean is null");
		Class<?> bc = Class.forName(uri.getHost());
		Bean b = new Bean(bean);
		if (!bc.isAssignableFrom(b.getBeanClass())) throw new IllegalArgumentException("Given URI class '"+bc.getName()+"' is not assignable from bean-class '"+b.getName()+"'");
		return b.invoke(bean, uri.getPath(), force, params);
	}
	
//	public static Object invokeBean(Object bean, String uri, Object ... params) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, UnsupportedEncodingException, URISyntaxException {
//		return invokeBean(bean, uri, false, params);
//	}
	
	public static Object invokeBean(Object bean, String uri, boolean force, Object ... params) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, UnsupportedEncodingException, URISyntaxException {
		int idx = uri.lastIndexOf('/');
		URI u = new URI(uri.substring(0,idx+1)+URLEncoder.encode(uri.substring(idx+1), "UTF-8"));
		return invokeBean(bean,u,force,params);
	}
	
	public static boolean isArray(Object obj) {
		return obj.getClass().isArray();
	}
	
	public static boolean isCollection(Object obj) {
		return Collection.class.isAssignableFrom(obj.getClass());
	}
	
	public static boolean isMap(Object obj) {
		return Map.class.isAssignableFrom(obj.getClass());
	}
	
	public static boolean isListType(Object obj) {
		return isArray(obj) || isCollection(obj) || isMap(obj);
	}
	
	@SuppressWarnings("unchecked")
	public static Object getFirstItem(Object ltype) {
		if (isArray(ltype)) {
			if (Array.getLength(ltype) > 0) 
				return Array.get(ltype, 0);
		} else if (isCollection(ltype)) {
			Collection<Object> c = (Collection<Object>)ltype;
			if (c.size() > 0) 
				return Iterables.get(c, 0);
		} else if (isMap(ltype)) {
			Map<Object, Object> m = (Map<Object, Object>)ltype;
			if (m.size() > 0) 
				return Iterables.get(m.values(), 0);
		} else return ltype;
		
		return null;
	}
	
	public static List<Class<?>> getGenerics(ParameterizedType pt) {
		List<Class<?>> ret = new ArrayList<Class<?>>();
		for (java.lang.reflect.Type t : pt.getActualTypeArguments())
			if (t instanceof Class) ret.add((Class<?>)t);
			else if (t instanceof ParameterizedType) ret.addAll(getGenerics((ParameterizedType)t));
		return ret;
	}
	
	public static Class<?> getGenericType(Class<?> clazz) {
		Type stype = clazz.getGenericSuperclass();
		return (Class<?>)((ParameterizedType)stype).getActualTypeArguments()[0];
	}
}
