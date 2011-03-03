Suppose you have an annotation like:

public @interface MyAnnotation {
	String aStringValue();
	boolean aBooleanValue();
}


You may use AnnotationBuilder to configure a new instance for it:

AnnotationBuilder<MyAnnotation> builder = AnnotationBuilder.create(MyAnnotation.class);
MyAnnotation mockAnn = builder.toMock(MyAnnotation.class);

builder.configure(mockAnn.aStringValue()).with("Some value");
builder.configure(mockAnn.aBooleanValue()).with(true);

//This is your instance annotation
MyAnnotation annInstance = builder.toAnnotation();
