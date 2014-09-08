package org.isf.commons.dto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.isf.commons.bean.Attribute;
import org.isf.commons.bean.Bean;
import org.isf.commons.bean.BeanFactory;

public class DefaultDTOConvertRule implements DTOConvertRule {

	@Override
	public <T> T marshall(Class<T> dtoClass, Object data) throws Exception {
		if (data == null) return null;
		
		T ret = dtoClass.newInstance();
		Bean dbean = BeanFactory.createBeanInstance(ret);
		Bean sbean = BeanFactory.createBeanInstance(data);
		
		List<Attribute> lattr = dbean.getChildrenAnnotatedWith(DTOAttribute.class);
		Collections.sort(lattr, new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				DTOAttribute a1 = o1.isAnnotationPresent(DTOAttribute.class) ? o1.getAnnotation(DTOAttribute.class) : null;
				DTOAttribute a2 = o2.isAnnotationPresent(DTOAttribute.class) ? o2.getAnnotation(DTOAttribute.class) : null;
				if (a1 == null && a2 == null) return 0;
				else if (a1 == null) return -1;
				else if (a2 == null) return 1;
				else if (a1.hierarchy() == a2.hierarchy()) return 0;
				else return a1.hierarchy() > a2.hierarchy() ? 1 : -1;
			}
		});
		
		for (Attribute attr : lattr) {
			DTOAttribute dtoa = attr.getAnnotation(DTOAttribute.class);
			if (!dtoa.assignableFrom().isAssignableFrom(data.getClass())) continue;
			Attribute sattr = sbean.getChildPerPath(dtoa.value());
			DTOInjectionData did = new DTOInjectionData(dbean, sbean, attr, sattr, ret, data, dtoa);
			DTOAttributeConvertRule rule = dtoa.convertRule().newInstance();
			attr.invoke(ret, "", dtoa.force(), rule.marshallAttribute(did));
		}
		return ret;
	}
	
	@Override
	public <T> T unmarshall(Object dto, T data) throws Exception {
		if (dto == null) return null;
		Bean sbean = BeanFactory.createBeanInstance(dto);
		Bean tbean = BeanFactory.createBeanInstance(data);
		
		List<Attribute> lattr = sbean.getChildrenAnnotatedWith(DTOAttribute.class);
		Collections.sort(lattr, new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				DTOAttribute a1 = o1.isAnnotationPresent(DTOAttribute.class) ? o1.getAnnotation(DTOAttribute.class) : null;
				DTOAttribute a2 = o2.isAnnotationPresent(DTOAttribute.class) ? o2.getAnnotation(DTOAttribute.class) : null;
				if (a1 == null && a2 == null) return 0;
				else if (a1 == null) return -1;
				else if (a2 == null) return 1;
				else if (a1.hierarchy() == a2.hierarchy()) return 0;
				else return a1.hierarchy() > a2.hierarchy() ? 1 : -1;
			}
		});
		
		for (Attribute sattr : lattr) {
			DTOAttribute dtoa = sattr.getAnnotation(DTOAttribute.class);
			if ((!dtoa.assignableFrom().isAssignableFrom(data.getClass())) || 
					dtoa.oneWay()) continue;
			Attribute tattr = tbean.getChildPerPath(dtoa.value());  // TODO canCreate = true ???
			if (tattr == null) continue; // TODO ACHTUNG
			DTOInjectionData did = new DTOInjectionData(tbean, sbean, tattr, sattr, data, dto, dtoa);
			DTOAttributeConvertRule rule = dtoa.convertRule().newInstance();
			// TODO beachte segment-rules !!!
			// tattr.invoke(data, "", rule.unmarshallAttribute(did));
//			System.out.println("DEBUG: inject "+dtoa.value());
			tbean.invoke(data, dtoa.value(), dtoa.force(), rule.unmarshallAttribute(did));
		}
		
		return data;
	}
	
}
