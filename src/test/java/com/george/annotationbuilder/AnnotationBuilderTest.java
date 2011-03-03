package com.george.annotationbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AnnotationBuilderTest {

	@Test
	public void testToAnnotation() {
		AnnotationBuilder<MyAnnotation> builder = AnnotationBuilder.create(MyAnnotation.class);
		MyAnnotation mockAnn = builder.toMock(MyAnnotation.class);

		builder.configure(mockAnn.aStringValue()).with("Some value");
		builder.configure(mockAnn.aBooleanValue()).with(true);

		// This is your instance annotation
		MyAnnotation annInstance = builder.toAnnotation();
		assertEquals(true, annInstance.aBooleanValue());
		assertEquals("Some value", annInstance.aStringValue());
	}

}
