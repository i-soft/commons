package org.isf.commons.mvc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBindingProvider<T> implements IController<T> {

	private Map<Object, ViewDataBindingSupport> viewBindings = new HashMap<Object, ViewDataBindingSupport>();
	
	protected DataBindingSupport handleDataBinding(ViewDataBindingSupport viewSupport, Object view, Object target, Class<?> targetClass, DataBinding binding) {
		DataBindingContext ctx = new DataBindingContext();
		ctx.setController(this);
		ctx.setView(view);
		ctx.setTarget(target);
		ctx.setTargetClass(targetClass);
		ctx.setTargetAttribute(binding.targetAttribute());
		ctx.setModelAttribute(binding.modelAttribute());
		
		Class<? extends IDataBindingRule> ruleClass = binding.rule();
		try {
			IDataBindingRule rule = ruleClass.newInstance();
			return rule.bind(ctx);
		} catch(IllegalAccessException iae) {
			throw new RuntimeException("Could not access Default-Constructor of Rule-Class '"+ruleClass.getName()+"'.");
		} catch(InstantiationException ie) {
			throw new RuntimeException("Could not create an instance of Rule-Class '"+ruleClass.getName()+"'.");
		}
	}
	
	protected ViewDataBindingSupport createViewDataBindingSupport(Object view) {
		return new ViewDataBindingSupport(this, view);
	}
	
	@Override
	public void adaptView(Object view) {
		if (view == null) throw new IllegalArgumentException("View is null...");
		ViewDataBindingSupport viewSupport = createViewDataBindingSupport(view);
		Class<?> viewClass = view.getClass();
		for (Field f : viewClass.getFields()) {
			if (f.isAnnotationPresent(DataBinding.class) || f.isAnnotationPresent(DataBindings.class)) {
				try {
					Object target = f.get(view);
					if (f.isAnnotationPresent(DataBinding.class)) {
						DataBinding binding = f.getAnnotation(DataBinding.class);
						viewSupport.addBinding(handleDataBinding(viewSupport, view, target, f.getType(), binding));
					} else if (f.isAnnotationPresent(DataBindings.class)) {
						DataBindings bindings = f.getAnnotation(DataBindings.class);
						for (DataBinding binding : bindings.value())
							viewSupport.addBinding(handleDataBinding(viewSupport, view, target, f.getType(), binding));
					}
				} catch(IllegalAccessException iae) {
					throw new IllegalArgumentException("Can not access field '"+f.getName()+"' on viewClass '"+viewClass.getName()+"'.", iae);
				}
			}
		}
		
		for (Method m : viewClass.getMethods()) {
			if (m.isAnnotationPresent(DataBinding.class) || m.isAnnotationPresent(DataBindings.class)) {
				try {
					Object target = m.invoke(view);
					if (m.isAnnotationPresent(DataBinding.class)) {
						DataBinding binding = m.getAnnotation(DataBinding.class);
						viewSupport.addBinding(handleDataBinding(viewSupport, view, target, m.getReturnType(), binding));
					} else if (m.isAnnotationPresent(DataBindings.class)) {
						DataBindings bindings = m.getAnnotation(DataBindings.class);
						for (DataBinding binding : bindings.value())
							viewSupport.addBinding(handleDataBinding(viewSupport, view, target, m.getReturnType(), binding));
					}
				} catch(IllegalAccessException iae) {
					throw new IllegalArgumentException("Can not access method '"+m.getName()+"' on viewClass '"+viewClass.getName()+"'.", iae);
				} catch(InvocationTargetException ite) {
					throw new IllegalArgumentException("Can not access method '"+m.getName()+"' on viewClass '"+viewClass.getName()+"'.", ite);
				}
			}
		}
		viewBindings.put(view, viewSupport);
	}

	@Override
	public void removeView(Object view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetViews(Object... views) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshViews(Object... views) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetAllViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshAllViews() {
		// TODO Auto-generated method stub

	}

}
