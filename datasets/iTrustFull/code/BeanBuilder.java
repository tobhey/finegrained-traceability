package edu.ncsu.csc.itrust;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Takes a parameter map and creates a bean from that map of the appropriate type.
 * @param <T> The type to be returned from the appropriate parameter map.
 */
public class BeanBuilder<T> {
	/**
	 * The code here is not obvious, but this method should not need rewriting unless a bug is found
	 * 
	 * @param map -
	 *            typically a request.getParameterMap; also can be a HashMap
	 * @param bean -
	 *            an instantiated bean to be loaded. Loaded bean is returned.
	 * @return a loaded "bean"
	 * @throws Exception -
	 *             Several exceptions are thrown here, so promotion seemed fitting
	 */
	// this warning is only suppressed because Map isn't parameterized (old JSP)
	@SuppressWarnings("unchecked")
	public T build(Map map, T bean) throws Exception {
		// JavaBeans should not have overloaded methods, according to their API
		// (a stupid limitation!)
		// Nevertheless, we should check for it
		checkOverloadedMethods(bean);

		// Use an introspector to find all of the getXXX or setXXX, we only want
		// the setXXX
		PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(bean.getClass())
				.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : propertyDescriptors) {
			// if object is null, either it was ignored or empty - just go with
			// bean's default
			String[] value = (String[]) map.get(descriptor.getName());
			Method writeMethod = descriptor.getWriteMethod();
			if (!"class".equals(descriptor.getName()) && value != null && writeMethod != null) {
				// descriptor's name is the name of your property; like
				// firstName
				// only take the first string
				try {
					// Skip the setters for enumerations
					if (writeMethod.getParameterTypes()[0].getEnumConstants() == null)
						writeMethod.invoke(bean, new Object[] { value[0] });
				} catch (IllegalArgumentException e) {
					// Throw a more informative exception
					throw new IllegalArgumentException(e.getMessage() + " with " + writeMethod.getName()
							+ " and " + value[0]);
				}
			}
		}
		return bean;
	}

	/**
	 * Checks for overloaded methods
	 * 
	 * @param bean item to check
	 */
	private void checkOverloadedMethods(T bean) {
		Method[] methods = bean.getClass().getDeclaredMethods();
		HashMap<String, String> nameMap = new HashMap<String, String>(methods.length);
		for (Method method : methods) {
			if (nameMap.get(method.getName()) != null)
				throw new IllegalArgumentException(bean.getClass().getName()
						+ " should not have any overloaded methods, like " + method.getName());
			if (!"equals".equals(method.getName())) // allow an equals override
				nameMap.put(method.getName(), "exists");
		}

	}
}
