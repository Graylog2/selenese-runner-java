package jp.vmi.selenium.selenese.result;

import com.saucelabs.saucerest.SauceREST;
import jp.vmi.selenium.selenese.inject.AbstractExecuteTestRunInterceptor;
import jp.vmi.selenium.webdriver.SauceRESTHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SauceLabsReportingTestSuiteInterceptor extends AbstractExecuteTestRunInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SauceLabsReportingTestSuiteInterceptor.class);

    @Override
    protected Result invoke(MethodInvocation invocation, String... filenames) throws Throwable {
        final Result result = (Result)invocation.proceed();

        if (SauceRESTHolder.getSauceREST() == null) {
            return result;
        }

        final SauceREST sauceREST = SauceRESTHolder.getSauceREST();
        final SessionId sessionId = SauceRESTHolder.getSessionId();

        if (result.isSuccess()) {
            log.info("Test Run passed, reporting success to Sauce Labs.");
            sauceREST.jobPassed(sessionId.toString());
        } else {
            log.info("Test Run failed, reporting failure to Sauce Labs.");
            sauceREST.jobFailed(sessionId.toString());
        }

        return result;
    }
}
