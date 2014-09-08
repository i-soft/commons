package org.isf.commons.dto;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.isf.commons.Util;
import org.isf.commons.bean.Bean;
import org.isf.commons.bean.BeanFactory;
import org.isf.commons.bean.Type;

public class DefaultDTOAttributeConvertRule implements DTOAttributeConvertRule {

	protected Object marshallAttributeValue(DTOInjectionData did, Object data) throws Exception {
		return (did.getTargetAttribute().getReturnType().isAnnotationPresent(DTO.class)) ? 
				DTOFactory.marshallDTO(did.getTargetAttribute().getReturnType(), data) : 
				data;		
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected Object marshallCollection(DTOInjectionData did) throws Exception {
		Collection tcol = (Collection)did.getTargetAttribute().invoke(did.getTargetObject(), "", did.getDTOAttribute().force());
		// TODO if tcol null create collection
		Object sdata = did.getSourceBean().invoke(did.getSourceObject(), did.getDTOAttribute().value(), did.getDTOAttribute().force());
		if (sdata == null) return null;
		switch(Type.getTypePerClass(sdata.getClass())) {
			case COLLECTION:
				Collection col = (Collection)sdata;
				for (Object o : col) {
					Object coldata = marshallAttributeValue(did, o);
					if (!tcol.contains(coldata))
						tcol.add(coldata);
				}
				break;
			case MAP:
				Map map = (Map)sdata;
				for (Object o : map.values()) {
					Object mapdata = marshallAttributeValue(did, o);
					if (!tcol.contains(mapdata))
						tcol.add(mapdata);
				}
				break;
			case ARRAY:
				for (int i=0;i<Array.getLength(sdata); i++) {
					Object arrdata = marshallAttributeValue(did, Array.get(sdata, i));
					if (!tcol.contains(arrdata))
						tcol.add(arrdata);
				}
				break;
			default:
				Object sndata = marshallAttributeValue(did, sdata);
				if (!tcol.contains(sndata))
					tcol.add(sndata);
		}
		return tcol;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected void put(DTOInjectionData did, Map map, Object data) throws Exception {
		if (did.getDTOAttribute().mapKey().length() > 0) {
			String mapKey = did.getDTOAttribute().mapKey();
			if (mapKey.startsWith(MAP_KEY_INVOKE)) {
//				Bean dataBean = new Bean(data);
				Bean dataBean = BeanFactory.createBeanInstance(data);
				map.put(dataBean.invoke(data, Util.cut(mapKey, MAP_KEY_INVOKE+"(", ")"), did.getDTOAttribute().force()), data);
			} else map.put(mapKey, data);
		}
		throw new IllegalArgumentException("DTOAttribute.mapKey must be set for Map-Types");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected Object marshallMap(DTOInjectionData did) throws Exception {
		Map tmap = (Map)did.getTargetAttribute().invoke(did.getTargetObject(), "", did.getDTOAttribute().force());
		// TODO if tmap null create map
		Object sdata = did.getSourceBean().invoke(did.getSourceObject(), did.getDTOAttribute().value(), did.getDTOAttribute().force());
		if (sdata == null) return null;
		switch(Type.getTypePerClass(sdata.getClass())) {
			case COLLECTION:
				Collection col = (Collection)sdata;
				for (Object o : col)
					put(did, tmap, marshallAttributeValue(did, o));
				break;
			case MAP:
				Map map = (Map)sdata;
				for (Object key : map.keySet()) {
					Object mapdata = marshallAttributeValue(did, map.get(key));
					if (did.getDTOAttribute().mapKey().length() > 0) put(did, tmap, mapdata);
					else tmap.put(key, mapdata);
				}					
				break;
			case ARRAY:
				for (int i=0;i<Array.getLength(sdata);i++)
					put(did, tmap, marshallAttributeValue(did, Array.get(sdata, i)));
				break;
			default:
				Object sndata = marshallAttributeValue(did, sdata);
				put(did, tmap, sndata);
		}
		return tmap;
	}
	
	protected Object marshallArray(DTOInjectionData did) throws Exception {
		// TODO implement 
		throw new IllegalArgumentException("not implemented yet");
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected Object marshallSingle(DTOInjectionData did) throws Exception {
		Object sdata = did.getSourceBean().invoke(did.getSourceObject(), did.getDTOAttribute().value(), did.getDTOAttribute().force());
		if (sdata == null) return null;
		switch(Type.getTypePerClass(sdata.getClass())) {
			case COLLECTION:
				Collection col = (Collection)sdata;
				Iterator<Object> it = col.iterator();
				return marshallAttributeValue(did, it.hasNext() ? it.next() : null);
			case MAP:
				throw new IllegalArgumentException("Could not marshall Map to single-value for field '"+did.getDTOAttribute().value()+"'");
			case ARRAY:
				return marshallAttributeValue(did, Array.getLength(sdata) > 0 ? Array.get(sdata, 0) : null);
			default:
				return marshallAttributeValue(did, sdata);
		}
	}
	
	@Override
	public Object marshallAttribute(DTOInjectionData did) throws Exception {
		switch (did.getTargetAttribute().getDataType()) {
			case COLLECTION:
				return marshallCollection(did);
			case MAP:
				return marshallMap(did);
			case ARRAY:
				return marshallArray(did);
			default:
				return marshallSingle(did);
		}
	}
	
	
	
	protected Object unmarshallAttributeValue(DTOInjectionData did, Object dtoData, Object refData) throws Exception {
		if (dtoData == null) return null;
		if (dtoData.getClass().isAnnotationPresent(DTO.class)) {
			if (refData == null) return DTOFactory.unmarshallDTO(dtoData, did.getTargetAttribute().getReturnType());
			else return DTOFactory.unmarshallDTO(dtoData, refData);
		} else return dtoData;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	protected Object unmarshallCollection(DTOInjectionData did) throws Exception {
//		System.out.println("UNMARSHALL COLLECTION: "+did.getDTOAttribute().value());
		Object dtoData = did.getSourceAttribute().invoke(did.getSourceObject(), "", did.getDTOAttribute().force());
		Collection refData = (Collection)did.getTargetBean().invoke(did.getTargetObject(), did.getDTOAttribute().value(), did.getDTOAttribute().force());
		switch(did.getSourceAttribute().getDataType()) {
			case COLLECTION:
			case MAP:
			case ARRAY:
			default: return null; // TODO implement
		
		}
	}
	
	protected Object unmarshallMap(DTOInjectionData did) throws Exception {
		throw new IllegalArgumentException("not implemented yet");
	}
	
	protected Object unmarshallArray(DTOInjectionData did) throws Exception {
		throw new IllegalArgumentException("not implemented yet");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object unmarshallSingle(DTOInjectionData did) throws Exception {
//		System.out.println("UNMARSHALL SINGLE: "+did.getDTOAttribute().value());
		Object dtoData = did.getSourceAttribute().invoke(did.getSourceObject(), "", did.getDTOAttribute().force());
		Object refData = did.getTargetBean().invoke(did.getTargetObject(), did.getDTOAttribute().value(), did.getDTOAttribute().force());
		switch(did.getSourceAttribute().getDataType()) {
			case COLLECTION:
				Collection col = (Collection)dtoData;
				Iterator<Object> it = col.iterator();
				return it.hasNext() ? unmarshallAttributeValue(did, it.next(), refData) : null;
			case MAP:
				throw new IllegalArgumentException("Could not unmarshall Map to single-value for field '"+did.getDTOAttribute().value()+"'");
			case ARRAY:
				return Array.getLength(dtoData) > 0 ? unmarshallAttributeValue(did, Array.get(dtoData, 0), refData) : null;
			default:
				return unmarshallAttributeValue(did, dtoData, refData);
		}
	}
	
	@Override
	public Object unmarshallAttribute(DTOInjectionData did) throws Exception {
		switch (did.getTargetAttribute().getDataType()) {
			case COLLECTION:
				return unmarshallCollection(did);
			case MAP:
				return unmarshallMap(did);
			case ARRAY:
				return unmarshallArray(did);
			default:
				return unmarshallSingle(did);
		}
	}

}
