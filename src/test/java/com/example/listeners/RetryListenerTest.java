package com.example.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import org.testng.internal.annotations.DisabledRetryAnalyzer;
import org.testng.internal.annotations.TestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RetryListenerTest {

    private final RetryListener listener = new RetryListener();

    @Test
    public void attachesAnalyzerWhenTestNGReportsNoAnalyzerAsDisabled() {
        ITestAnnotation annotation = new TestAnnotation();
        annotation.setRetryAnalyzer(DisabledRetryAnalyzer.class);

        transform(annotation, "testWithNoAnalyzer");

        assertThat(annotation.getRetryAnalyzerClass(), is(RetryAnalyzer.class));
    }

    @Test
    public void attachesAnalyzerWhenNoAnalyzerIsPresentAtAll() {
        ITestAnnotation annotation = new TestAnnotation();

        transform(annotation, "testWithNoAnalyzer");

        assertThat(annotation.getRetryAnalyzerClass(), is(RetryAnalyzer.class));
    }

    @Test
    public void leavesAnalyzerThatTheTestAlreadyDeclared() {
        ITestAnnotation annotation = new TestAnnotation();
        annotation.setRetryAnalyzer(OwnAnalyzer.class);

        transform(annotation, "testWithOwnAnalyzer");

        assertThat(annotation.getRetryAnalyzerClass(), is(OwnAnalyzer.class));
    }

    @Test
    public void analyzerGivesUpAfterTwoRetries() {
        RetryAnalyzer analyzer = new RetryAnalyzer();

        assertThat(analyzer.retry(null), is(true));
        assertThat(analyzer.retry(null), is(true));
        assertThat(analyzer.retry(null), is(false));
    }

    private void transform(ITestAnnotation annotation, String methodName) {
        try {
            Method method = Sample.class.getMethod(methodName);
            listener.transform(annotation, Sample.class, (Constructor<?>) null, method);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static class Sample {

        public void testWithNoAnalyzer() {
        }

        public void testWithOwnAnalyzer() {
        }
    }

    public static class OwnAnalyzer implements IRetryAnalyzer {
        @Override
        public boolean retry(org.testng.ITestResult result) {
            return false;
        }
    }
}
