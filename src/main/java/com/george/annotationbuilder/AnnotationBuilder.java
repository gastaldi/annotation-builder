package com.george.annotationbuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class AnnotationBuilder<T extends Annotation> {

	private Class<T> ann;
	private String currentAnnMethod;

	private final Map<String, Object> props = new HashMap<String, Object>();

	public static <T extends Annotation> AnnotationBuilder<T> create(Class<T> ann) {
		return new AnnotationBuilder<T>(ann);
	}

	public AnnotationBuilder(Class<T> ann) {
		this.ann = ann;
	}

	@SuppressWarnings("unchecked")
	public <M extends Annotation> M toMock(Class<M> cl) {
		return (M) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { cl }, new NullInvocationHandler());
	}

	public <R> BuilderResult<R> configure(R object) {
		return new BuilderResult<R>();
	}

	@SuppressWarnings("unchecked")
	public T toAnnotation() {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { ann }, new PropertyInvocationHandler(props));
	}

	public class BuilderResult<R> {
		public AnnotationBuilder<T> with(R value) {
			props.put(currentAnnMethod, value);
			return AnnotationBuilder.this;
		}
	}

	private class NullInvocationHandler implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			currentAnnMethod = method.getName();
			Object defaultValue = method.getDefaultValue();
			Class<?> returnType = method.getReturnType();
			if (defaultValue == null &&  returnType.isPrimitive()) {
				if (returnType == Integer.TYPE) {
					defaultValue = 0;
				} else if (returnType == Boolean.TYPE) {
					defaultValue = false;
				} else if (returnType == Float.TYPE) {
					defaultValue = 0.0f;
				} else if (returnType == Double.TYPE) {
					defaultValue = 0.0d;
				} else if (returnType == Short.TYPE) {
					defaultValue = (short) 0;
				} else if (returnType == Character.TYPE) {
					defaultValue = ' ';
				} else if (returnType == Long.TYPE) {
					defaultValue = 0L;
				}
			}
			return defaultValue;
		}
	}

	private static class PropertyInvocationHandler implements InvocationHandler {
		Map<String, Object> props;

		public PropertyInvocationHandler(Map<String, Object> props) {
			this.props = props;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return props.get(method.getName());
		}
	}

}
