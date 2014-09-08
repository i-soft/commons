package org.isf.commons;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JPAUtil {

	private static BeanManager manager;
	
	public static BeanManager getBeanManager() {
		if (manager != null) return manager;
		try {
			final InitialContext ctx = new InitialContext();
			manager = (BeanManager)ctx.lookup("java:comp/BeanManager");
		} catch(NamingException ne) {
			throw new RuntimeException("Can not locate BeanManager...", ne);
		}
		return manager;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T injectBean(Class<T> clazz) throws InjectionException {
		try {
			BeanManager bm = getBeanManager();		
			Bean<T> bean = (Bean<T>)bm.getBeans(clazz).iterator().next();
			CreationalContext<T> ctx = bm.createCreationalContext(bean);
			return clazz.cast(bm.getReference(bean, clazz, ctx));
		} catch(Throwable t) {
			throw new InjectionException("Could not inject CDI BeanManager for Type '"+clazz.getName()+"'", t);
		}
	}
	
	public static <T> T injectResource(Class<T> clazz, String mappedName) throws InjectionException {
		try {
			final InitialContext ctx = new InitialContext();
			return clazz.cast(ctx.lookup(mappedName));
		} catch(NamingException ne) {
			throw new InjectionException("Can not locate Resource '"+mappedName+"'...", ne);
		}
	}
	
}
