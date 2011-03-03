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

	public static <T> AnnotationBuilder create(Class<T> ann) {
		return new AnnotationBuilder(ann);
	}

	public AnnotationBuilder(Class<T> ann) {
		this.ann = ann;
	}

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
			return method.getDefaultValue();
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
