package jp.vmi.selenium.webdriver;

import com.saucelabs.saucerest.SauceREST;
import org.openqa.selenium.remote.SessionId;

public class SauceRESTHolder {
    private static SauceREST sauceREST;
    private static SessionId sessionId;

    public static SauceREST getSauceREST() {
        return sauceREST;
    }

    public static void setSauceREST(SauceREST sauceREST) {
        SauceRESTHolder.sauceREST = sauceREST;
    }

    public static SessionId getSessionId() {
        return sessionId;
    }

    public static void setSessionId(SessionId sessionId) {
        SauceRESTHolder.sessionId = sessionId;
    }
}
