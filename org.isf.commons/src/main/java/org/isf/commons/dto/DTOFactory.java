package org.isf.commons.dto;

import java.io.File;
import java.lang.reflect.Constructor;

import org.isf.commons.bean.Attribute;
import org.isf.commons.bean.Bean;
import org.isf.commons.bean.BeanFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class DTOFactory {

	public static final String DTO_FACTORY_DIRECTORY = "dto.factory.dir";
	private static final String DTO_SUPPORT = "_DTOSupport";
	
	private static final ClassPool pool;
	static {
		pool = ClassPool.getDefault();  // TODO user-temp bzw. user classpath
		if (!System.getProperties().containsKey(DTO_FACTORY_DIRECTORY))
			System.setProperty(DTO_FACTORY_DIRECTORY, System.getProperty("java.io.tmpdir")+"/oers-classes");
		System.out.println(System.getProperty(DTO_FACTORY_DIRECTORY));
		File f = new File(System.getProperty(DTO_FACTORY_DIRECTORY));
		if (!f.exists()) f.mkdirs();
		try {
			pool.appendPathList(System.getProperty(DTO_FACTORY_DIRECTORY));
		} catch(NotFoundException nfe) {
			nfe.printStackTrace();
		}
	}
	
	public static <T> T marshallDTO(Class<T> dtoClass, Object data) throws Exception {
		if (data == null) return null;
		if (!dtoClass.isAnnotationPresent(DTO.class)) throw new IllegalArgumentException("DTO Annotation is not present.");
		DTO dcl = dtoClass.getAnnotation(DTO.class);
//		if (!dcl.value().isAssignableFrom(data.getClass())) throw new IllegalArgumentException(dcl.value().getName()+" is not assignable from "+data.getClass().getName());
		DTOConvertRule dcr = dcl.convertRule().newInstance();
		T ret = dcr.marshall(dtoClass, data);
		if (dcl.propertySupport()) ret = createDTOSupportInstance(ret);
		return ret;
	}
	
	public static <T> T unmarshallDTO(Object dto, Class<T> dataClass) throws Exception {
		return unmarshallDTO(dto, dataClass.newInstance());
	}
	
	public static <T> T unmarshallDTO(Object dto, T data) throws Exception {
		if (data == null) return null;
		if (dto == null) throw new NullPointerException("DTO-Object is null");
		Class<?> dtoClass = dto.getClass();
		if (!dtoClass.isAnnotationPresent(DTO.class)) throw new IllegalArgumentException("DTO Annotation is not present.");
		DTO dcl = dtoClass.getAnnotation(DTO.class);
		DTOConvertRule dcr = dcl.convertRule().newInstance();
		return dcr.unmarshall(dto, data);
	}
	
	public static Class<?> createDTOSupportClass(Class<?> dtoClass) throws Exception {
		if (!dtoClass.isAnnotationPresent(DTO.class)) throw new IllegalArgumentException("DTO Annotation is not present.");
		String dtoClassName = dtoClass.getName();		
		String dtoSpClassName = dtoClassName+DTO_SUPPORT;
		String dtoSpSimpleName = dtoClass.getSimpleName()+DTO_SUPPORT;
		Class<?> cls = null;
		try {
			cls = Class.forName(dtoSpClassName);
		} catch(ClassNotFoundException cnfe) {
			CtClass dtoCt = pool.getOrNull(dtoClassName);
			if (dtoCt == null) {
				pool.appendClassPath(new LoaderClassPath(dtoClass.getClassLoader()));
				dtoCt = pool.get(dtoClassName);
			}
			CtClass dtoSp = pool.getOrNull(dtoSpClassName);
			if (dtoSp == null) {
				Bean b = BeanFactory.createBeanInstance(dtoClass);
				dtoSp = pool.makeClass(dtoSpClassName, dtoCt);
				/* PropertyChangeListener */
				dtoSp.addInterface(pool.get("org.isf.commons.dto.DTOSupport"));
				CtField sver = CtField.make("private static final long serialVersionUID = 1L;", dtoSp);  // TODO random serialVersionUID
				dtoSp.addField(sver);
				
				CtField chglist = CtField.make("private java.util.List changes = new java.util.ArrayList();", dtoSp);
				dtoSp.addField(chglist);
				
				CtMethod chghis = CtNewMethod.make("public org.isf.commons.dto.DTOChangeEvent[] listChanges() { " +
						"org.isf.commons.dto.DTOChangeEvent[] ret = new org.isf.commons.dto.DTOChangeEvent[changes.size()];" +
						"for (int i=0;i<changes.size();i++) " +
						"ret[i] = changes.get(i);" +
						"return ret; " +
						"}", dtoSp);
				dtoSp.addMethod(chghis);
				
				String pchgsrc = "public void propertyChange(java.beans.PropertyChangeEvent evt) {" +
							"changes.add(new  org.isf.commons.dto.DTOChangeEvent(evt));" +
							"java.lang.System.out.println(\"property: \"+evt.getPropertyName()+\" changed - oldValue: \"+evt.getOldValue()+\" newValue: \"+evt.getNewValue()); " +
						"}";
				CtMethod pchg = CtNewMethod.make(pchgsrc, dtoSp);
				dtoSp.addMethod(pchg);
				
				/* PropertyChangeSupport */
				CtField listener = CtField.make("private java.util.List listener = new java.util.ArrayList();", dtoSp);
				dtoSp.addField(listener);
				
				CtMethod addpl = CtNewMethod.make("public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) { listener.add(pcl); }", dtoSp);
				dtoSp.addMethod(addpl);
				
				CtMethod rmpl = CtNewMethod.make("public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) { listener.remove(pcl); }", dtoSp);
				dtoSp.addMethod(rmpl);
				
				CtConstructor cst = CtNewConstructor.make("public "+dtoSpSimpleName+"() { super(); addPropertyChangeListener(this); }", dtoSp);
				dtoSp.addConstructor(cst);
				
				String firesrc = "public void firePropertyChangeEvent(String property, Object oldValue, Object newValue) { " +
						"for (int i = 0;i<listener.size(); i++) {" +
							"if (listener.get(i) instanceof java.beans.PropertyChangeListener) {" +
								"java.beans.PropertyChangeListener li = (java.beans.PropertyChangeListener)listener.get(i); " +
								"li.propertyChange(new java.beans.PropertyChangeEvent(this,property,oldValue,newValue)); " +
							"}" +
						"}" +
						"}";
				CtMethod fire = CtNewMethod.make(firesrc, dtoSp);
				dtoSp.addMethod(fire);
				
				String cnstsrc = "public "+dtoSpSimpleName+"("+dtoClassName+" master) { super(); ";
				
				for (Attribute attr : b.getChildren()) {
					if (attr.getWriteMethod() == null) continue;
					CtMethod cm = CtNewMethod.make("public void "+attr.getWriteMethod().getName()+"("+attr.getWriteMethod().getParameterTypes()[0].getName()+" arg0) {" +
							"java.lang.Object oldValue = "+attr.getReadMethod().getName()+"(); " +
							"if ((oldValue == null && arg0 != null) || (oldValue != null && !oldValue.equals(arg0))) { " +
							"$proceed($1); " +
//							"super."+attr.getWriteMethod().getName()+"(arg0); " +
							"firePropertyChangeEvent(\""+attr.getName()+"\", oldValue, arg0); " +
							"} " +
							"}", dtoSp, "super", attr.getWriteMethod().getName());
					dtoSp.addMethod(cm);
					
					cnstsrc += attr.getWriteMethod().getName()+"(master."+attr.getReadMethod().getName()+"());";
				}
				
				cnstsrc += "addPropertyChangeListener(this); }";
				
				CtConstructor cnst = CtNewConstructor.make(cnstsrc, dtoSp);
				dtoSp.addConstructor(cnst);
				
				dtoSp.writeFile(System.getProperty(DTO_FACTORY_DIRECTORY));
			}
			cls = dtoSp.toClass();
		}
		
		return cls;
	}
	
	public static <T> T createDTOSupportInstance(T dto) throws Exception {
		if (dto == null) return null;
		@SuppressWarnings("unchecked")
		Class<T> dtoClass = (Class<T>)dto.getClass();
		Class<?> cls = createDTOSupportClass(dtoClass);
		
		Constructor<?> c = cls.getConstructor(dtoClass);
		return dtoClass.cast(c.newInstance(dto));
		//return dtoClass.cast(cls.newInstance());
	}
	
}
