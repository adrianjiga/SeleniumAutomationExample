package com.example.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.DisabledRetryAnalyzer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {

    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        Class<? extends IRetryAnalyzer> current = annotation.getRetryAnalyzerClass();
        if (current == null || DisabledRetryAnalyzer.class.isAssignableFrom(current)) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
