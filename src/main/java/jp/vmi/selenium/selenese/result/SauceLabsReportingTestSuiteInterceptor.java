package jp.vmi.selenium.selenese.result;

import com.saucelabs.saucerest.SauceREST;
import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.Selenese;
import jp.vmi.selenium.selenese.TestSuite;
import jp.vmi.selenium.selenese.inject.AbstractExecuteTestSuiteInterceptor;
import jp.vmi.selenium.webdriver.SauceRESTHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SauceLabsReportingTestSuiteInterceptor extends AbstractExecuteTestSuiteInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SauceLabsReportingTestSuiteInterceptor.class);

    @Override
    protected Result invoke(MethodInvocation invocation, TestSuite testSuite, Selenese parent, Context context) throws Throwable {
        final Result result = (Result)invocation.proceed();

        if (SauceRESTHolder.getSauceREST() == null) {
            return result;
        }

        final SauceREST sauceREST = SauceRESTHolder.getSauceREST();
        final SessionId sessionId = SauceRESTHolder.getSessionId();

        if (testSuite.isError()) {
            log.info("Test Suite failed, reporting failure to Sauce Labs.");
            sauceREST.jobFailed(sessionId.toString());
        } else {
            log.info("Test Suite passed, reporting success to Sauce Labs.");
            sauceREST.jobPassed(sessionId.toString());
        }

        return result;
    }
}
