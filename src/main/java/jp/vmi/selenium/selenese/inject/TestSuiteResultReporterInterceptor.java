package jp.vmi.selenium.selenese.inject;

import jp.vmi.junit.result.ITestTarget;
import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.Selenese;
import jp.vmi.selenium.selenese.TestSuite;
import jp.vmi.selenium.selenese.result.Result;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TestSuiteResultReporterInterceptor extends AbstractExecuteTestSuiteInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TestSuiteResultReporterInterceptor.class);

    @Override
    protected Result invoke(MethodInvocation invocation, TestSuite testSuite, Selenese parent, Context context) throws Throwable {
        final Result result = (Result)invocation.proceed();

        final List<Selenese> failures = testSuite.getSeleneseList().stream()
                .filter(ITestTarget::isError)
                .collect(Collectors.toList());

        if (!failures.isEmpty()) {
            log.warn("Test Failures:");
            failures.forEach(failure -> log.warn("Test: {}, Result: {}", failure.getType(), failure.getResult()));
        }

        return result;
    }
}
