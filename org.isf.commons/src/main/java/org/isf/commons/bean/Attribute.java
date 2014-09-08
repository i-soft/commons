package org.isf.commons.bean;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.isf.commons.Util;

public class Attribute implements ReflectionDescriptor<Bean, Attribute> {
	
	private ReflectionDescriptor<Bean, Attribute> parent;
	private Method getter;
	private Method setter;
	
	private List<Attribute> children;
	
	public Attribute(ReflectionDescriptor<Bean, Attribute> parent, Method getter, Method setter) {
		setParent(parent);
		if (getter == null) throw new IllegalArgumentException("A getter-method must be defined.");
		if (!(setter != null && getter.getReturnType().equals(setter.getParameterTypes()[0])))
			throw new IllegalArgumentException("getter return-value '"+getter.getReturnType().getName()+"' is not equal to setter param-type '"+setter.getParameterTypes()[0].getName()+"'");
		setReadMethod(getter);
		setWriteMethod(setter);
	}
	
	public Attribute(ReflectionDescriptor<Bean, Attribute> parent, Method getter) {
		this(parent, getter, null);
	}
	
	public Method getWriteMethod() { return setter; }
	protected void setWriteMethod(Method setter) { 
		this.setter = setter; 
	}
	
	public Method getReadMethod() { return getter; }
	protected void setReadMethod(Method getter) { 
		this.getter = getter; 
		
	}
	
	public Class<?> getType() {
		return getReadMethod().getReturnType();
	}
	
	public Class<?> getReturnType() {
		Class<?>[] genTypes = getGenericTypes();
		if (genTypes != null && genTypes.length > 0) return genTypes[0];
		else return getType();
	}
	
	public boolean hasGenericTypes() {
		return getGenericTypes() != null && getGenericTypes().length > 0;
	}
	
	public Class<?>[] getGenericTypes() {
		if (getReadMethod().getGenericReturnType() instanceof ParameterizedType) {
			List<Class<?>> l = Bean.getGenerics((ParameterizedType)getReadMethod().getGenericReturnType());
			return l.toArray(new Class<?>[0]);
		}
		return new Class<?>[0];
	}
	
	public Type getDataType() { return Type.getTypePerClass(getType()); }
	
	public Type getReturnDataType() { return Type.getTypePerClass(getReturnType()); }
	
	public Type[] getGenericDataTypes() {
		Class<?>[] gc = getGenericTypes();
		Type[] ret = new Type[gc.length];
		for (int i=0;i<gc.length;i++)
			ret[i] = Type.getTypePerClass(gc[i]);
		return ret;
	}
	
	public boolean isListType() {
		return isArrayType() || isCollectionType() || isMapType();
	}
	
	public boolean isCollectionType() {
		Type dt = getDataType();
		return dt == Type.COLLECTION;
	}
	
	public boolean isMapType() { 
		Type dt = getDataType();
		return dt == Type.MAP;
	}
	
	public boolean isArrayType() {
		Type dt = getDataType();
		return dt == Type.ARRAY;
	}
	
	public boolean isBeanType() {
		Type dt = getDataType();
		return dt == Type.OBJECT;
	}
	
	public boolean isReadable() { return getReadMethod() != null; }
	public boolean isWritable() { return getWriteMethod() != null; }
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> arg0) {
		T ret = getReadMethod().getAnnotation(arg0);
		if (ret != null) return ret;
		else return getWriteMethod().getAnnotation(arg0);
	}

	@Override
	public Annotation[] getAnnotations() {
		return getReadMethod().getAnnotations();
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getReadMethod().getDeclaredAnnotations();
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> arg0) {
		boolean ret = getReadMethod().isAnnotationPresent(arg0);
		if (!ret) ret = getWriteMethod().isAnnotationPresent(arg0);
		return ret;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public String getName() {
		return Util.normalizeName(getReadMethod().getName());
	}

	@Override
	public String getPath() {
		String ppath = getParent().getPath();
		return ppath+(ppath.length() > 0 ? separator : "")+getName();
	}

	@Override
	public ReflectionDescriptor<Bean, Attribute> getParent() {
		return parent;
	}
	protected void setParent(ReflectionDescriptor<Bean, Attribute> parent) { this.parent = parent; }

	@Override
	public Bean getRoot() {
		return getParent().getRoot();
	}
	
//	protected String getURIPath() {
//		String ppath = getParent().getPath();
//		return File.separator+ppath+(ppath.length() > 0 ? File.separator : "")+getName();
//	}
	
	public URI getURI() { 
		URI uri = getRoot().getURI();
		try {
			return new URI(uri.getScheme(),uri.getHost(), File.separator+URLEncoder.encode(getPath(), "UTF-8"), getDataType().name());
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean hasChildren() { return getChildren().size() > 0; }
	
	protected List<Attribute> getChildren(Class<?> type, Class<?>[] gtypes) {
		List<Attribute> ret = new ArrayList<Attribute>();
		switch (Type.getTypePerClass(type)) {
			case ARRAY:
				ret.addAll(getChildren(type.getSuperclass(), new Class<?>[0]));
				break;
			case COLLECTION:
				ret.addAll(getChildren(gtypes[0], new Class<?>[0]));
				//ret.addAll(Bean.initialMethods(cl, this));
				break;
			case MAP:
				ret.addAll(getChildren(gtypes[1], new Class<?>[0]));
				//ret.addAll(Bean.initialMethods(cl, this));
				break;
			case OBJECT:
				ret.addAll(Bean.initialMethods(type, this));
				break;
			default: 
		}
		return ret;
	}
	
	@Override
	public List<Attribute> getChildren() {
		if (children == null) {
			children = new ArrayList<Attribute>();
			children.addAll(getChildren(getType(), getGenericTypes()));
		}
		return children;
	}

	@Override
	public Attribute getChild(String name) {
		for (Attribute a : getChildren())
			if (name.equals(a.getName())) 
				return a;
		return null;
	}
	
	public Object invoke(Object bean, String path, boolean force, Object ... params) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		String currentsegment = Util.firstSegment(path, separator);
		path = Util.cutFirstSegment(path, separator);
		String nextsegment = Util.firstSegment(path, separator);
		
//		System.out.println("currentsegment: '"+currentsegment+"'");
//		System.out.println("nextsegment: '"+nextsegment+"'");
		
		Object data = getReadMethod().invoke(bean);
		// CanCreate
		if (isAnnotationPresent(AttributeInjection.class)) {
			AttributeInjection cc = getAnnotation(AttributeInjection.class);
			IAttributeInjectionDelegate delegate = cc.value().newInstance();
			try {
				data = delegate.injectAttribute(new AttributeInjectionData(this, bean, data, params));
			} catch(Exception e) {
				throw new InvocationTargetException(e);
			}
		}
		
		if (data == null && force && isWritable() && path != null && path.length() > 0 && params.length > 0) {
			data = getType().newInstance();
			getWriteMethod().invoke(bean, data);
		}
		
		SegmentEncoder enc = new SegmentEncoder(currentsegment);
		Object ret = enc.getData(data);
		
		if (path != null && path.length() > 0) {
			// invoke next attribute
	
			Attribute attr = getChild(SegmentEncoder.getRealSegment(nextsegment));
			if (attr == null) 
				if (!force) throw new IllegalArgumentException("Attribute '"+getName()+"' attribute '"+Util.firstSegment(path, separator)+"' unknown");
				else return null;
			
			if (Bean.isListType(ret)) 
				ret = Bean.getFirstItem(ret);
			
			if (ret == null)
				return null;
			
			return attr.invoke(ret, path, force, params);
			
		} else if (params != null && params.length > 0) {
			// local setter
			if (getWriteMethod() == null) throw new IllegalAccessException("Can not access write method for attribute '"+getName()+"'");
			// TODO ARRAYS, COLLECTIONS, MAPS
			getWriteMethod().invoke(bean, params);
			return null; // write call returns null
		} else return ret;
	}
	
	
}
