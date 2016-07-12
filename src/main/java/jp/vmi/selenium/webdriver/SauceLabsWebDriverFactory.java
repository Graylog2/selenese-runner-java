package jp.vmi.selenium.webdriver;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.saucelabs.saucerest.SauceREST;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.SAUCE_LABS_BUILD_NAME;
import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.SAUCE_LABS_CUSTOM_DATA;
import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.SAUCE_LABS_KEY;
import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.SAUCE_LABS_RUN_NAME;
import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.SAUCE_LABS_TUNNEL_IDENTIFIER;
import static jp.vmi.selenium.webdriver.DriverOptions.DriverOption.SAUCE_LABS_USER;

public class SauceLabsWebDriverFactory extends RemoteWebDriverFactory {
    private static final Logger log = LoggerFactory.getLogger(SauceLabsWebDriverFactory.class);
    private static final String CK_SAUCE_LABS_URL_SUFFIX = "@ondemand.saucelabs.com:80/wd/hub";

    @Override
    public WebDriver newInstance(DriverOptions driverOptions) {
        final DesiredCapabilities caps = getDesiredCapabilities(driverOptions);
        if (driverOptions.has(SAUCE_LABS_TUNNEL_IDENTIFIER)) {
            final String tunnelIdentifier = driverOptions.get(SAUCE_LABS_TUNNEL_IDENTIFIER);
            caps.setCapability("tunnel-identifier", tunnelIdentifier);
            log.info("Sauce Connect tunnel identifier: {}", tunnelIdentifier);
        }

        final String username = driverOptions.get(SAUCE_LABS_USER);
        final String key = driverOptions.get(SAUCE_LABS_KEY);
        final URL remoteUrl;

        if (!Strings.isNullOrEmpty(username) || !Strings.isNullOrEmpty(key)) {
            remoteUrl = getRemoteUrl(username, key);
        } else {
            throw new IllegalArgumentException("Sauce Labs username or key is missing!");
        }

        if (driverOptions.has(SAUCE_LABS_RUN_NAME)) {
            final String runName = driverOptions.get(SAUCE_LABS_RUN_NAME);
            caps.setCapability("name", runName);
            log.info("Sauce Labs run name: {}", runName);
        }

        if (driverOptions.has(SAUCE_LABS_CUSTOM_DATA)) {
            final String customData = driverOptions.get(SAUCE_LABS_CUSTOM_DATA);
            final Splitter splitter = Splitter.on(",");
            splitter.splitToList(customData).forEach(keyValuePair -> {
                final List<String> splitted = Splitter.on("=").splitToList(keyValuePair);
                if (splitted.size() == 2) {
                    final String k = splitted.get(0);
                    final String v = splitted.get(1);
                    if (caps.getCapability(k) != null) {
                        caps.setCapability(k, v);
                        log.info("Sauce Labs custom data: {}={}", k, v);
                    }
                }
            });
        }

        if (driverOptions.has(SAUCE_LABS_BUILD_NAME)) {
            final String buildName = driverOptions.get(SAUCE_LABS_BUILD_NAME);
            caps.setCapability("build", buildName);
            log.info("Sauce Labs build name: {}", buildName);
        }

        final RemoteWebDriver webDriver = getRemoteWebDriver(remoteUrl, caps, driverOptions);

        SauceRESTHolder.setSauceREST(new SauceREST(username, key));
        SauceRESTHolder.setSessionId(webDriver.getSessionId());

        return webDriver;
    }

    private URL getRemoteUrl(String username, String key) {
        try {
            return new URL("http://" + username + ":" + key + CK_SAUCE_LABS_URL_SUFFIX);
        } catch (MalformedURLException e) {
            log.error("Unable to parse generated Sauce Labs URL: ", e);
            return null;
        }
    }
}
