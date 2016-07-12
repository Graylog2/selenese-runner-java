package jp.vmi.selenium.selenese.result;

import com.saucelabs.saucerest.SauceREST;
import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.Selenese;
import jp.vmi.selenium.selenese.TestSuite;
import jp.vmi.selenium.selenese.inject.AbstractExecuteTestSuiteInterceptor;
import jp.vmi.selenium.webdriver.SauceRESTHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.remote.SessionId;

public class SauceLabsReportingTestSuiteInterceptor extends AbstractExecuteTestSuiteInterceptor {
    @Override
    protected Result invoke(MethodInvocation invocation, TestSuite testSuite, Selenese parent, Context context) throws Throwable {
        final Result result = (Result)invocation.proceed();

        /*if (!testSuite.getWebDriverName().equals("saucelabs")) {
            return result;
        }*/

        final SauceREST sauceREST = SauceRESTHolder.getSauceREST();
        final SessionId sessionId = SauceRESTHolder.getSessionId();

        if (testSuite.isError()) {
            sauceREST.jobFailed(sessionId.toString());
        } else {
            sauceREST.jobPassed(sessionId.toString());
        }

        return result;
    }
}
