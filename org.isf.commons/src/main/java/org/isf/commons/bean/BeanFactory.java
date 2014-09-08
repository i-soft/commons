package org.isf.commons.bean;

public class BeanFactory {

	public static Bean createBeanInstance(Class<?> clazz) {
		return new Bean(clazz);
	}
	
	public static Bean createBeanInstance(Object obj) {
		return new Bean(obj);
	}
	
	public static Bean createBeanInstance(String className) throws ClassNotFoundException {
		return new Bean(className);
	}
	
//	public static BeanWrapper wrapBean(Object obj) throws InvocationTargetException, IllegalAccessException, InstantiationException {
//		Bean b = new Bean(obj);
//		BeanWrapper bw = new BeanWrapper();
//		bw.setClassname(b.getName());
//		bw.setName(b.getDisplayName());		
//		
//		for (Attribute attr : b.getChildren()) {
//			if (attr.getDataType() == Type.OBJECT) {
//				
//			} else {
//				Property p = new Property();
//				p.setName(attr.getName());
//				p.setValue(Converter.convert(String.class, b.invoke(obj, attr.getPath(), true)));
//				bw.addProperty(p);
//			}
//		}
//		
//		return bw;
//	}
	
	

}
