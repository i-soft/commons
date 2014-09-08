package org.isf.commons.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

public class BeanBindings implements Bindings {
	
	private Object obj;
	private Bean bean;
	private Map<String, Object> vars = new HashMap<String, Object>();
	
	public BeanBindings(Object obj) {
		setObject(obj);
	}
	
	public Bean getBean() { return bean; }
	private void setBean(Bean bean) { this.bean = bean; }
	
	public Object getObject() { return obj; }
	public void setObject(Object obj) {
		this.obj = obj;
		if (obj != null) setBean(new Bean(obj));
	}

	@Override
	public void clear() {  }

	@Override
	public boolean containsValue(Object arg0) {
		throw new IllegalArgumentException("not suported");
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new IllegalArgumentException("not suported");
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Set<String> keySet() {
		Set<String> ret = vars.keySet();
		if (getBean() != null)
			for (Attribute attr : getBean().getChildren())
				ret.add(attr.getName()); // TODO subelements?
		return null;
	}

	@Override
	public int size() {
		return vars.size() + (getBean() != null ? getBean().getChildren().size() : 0); // TODO subelements?
	}

	@Override
	public Collection<Object> values() {
		throw new IllegalArgumentException("not suported");
	}

	@Override
	public boolean containsKey(Object key) {
		boolean ret = getBean() != null ? getBean().getChild((String)key) != null : false;  // TODO subelements?
		if (ret) return ret;
		else return vars.containsKey(key);
	}

	@Override
	public Object get(Object key) {
		if (getBean() != null) {
			Attribute attr = getBean().getChild((String)key);
			if (attr != null) {
				try {
					return getBean().invoke(getObject(), (String)key, false);
				} catch(Exception e) {
					/* DO NOTHING ??? */
				}
			}
		}
		return vars.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return vars.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> arg0) {
		vars.putAll(arg0);
	}

	@Override
	public Object remove(Object key) {
		return vars.remove(key);
	}

}
