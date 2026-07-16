package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/** Настраивает браузер, авторизацию и завершение сессии для UI-тестов. */
public abstract class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    private static final String BROWSER_NAME = "chrome";
    private static final String BROWSER_SIZE = "1920x1080";
    private static final String PAGE_LOAD_STRATEGY = "none";
    private static final String BROWSER_SETUP_LOG_MESSAGE = "Настройка браузера перед тестом";
    private static final String DATA_CLEANUP_LOG_MESSAGE = "Очистка данных после теста";
    private static final String BROWSER_CLOSING_LOG_MESSAGE = "Закрытие браузера после теста";
    private static final Duration ELEMENT_WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final boolean HEADLESS_MODE_ENABLED = false;

    private final AuthService authService = new AuthService();
    protected final AccountStateService accountStateService = new AccountStateService();

    @BeforeEach
    public void setUp() {
        LOGGER.info(BROWSER_SETUP_LOG_MESSAGE);
        Configuration.browser = BROWSER_NAME;
        Configuration.browserSize = BROWSER_SIZE;
        Configuration.pageLoadStrategy = PAGE_LOAD_STRATEGY;
        Configuration.timeout = ELEMENT_WAIT_TIMEOUT.toMillis();
        Configuration.headless = HEADLESS_MODE_ENABLED;
        Configuration.browserCapabilities = AuthService.createBrowserOptions();

        authService.openHomePageAndEnsureUserLoggedIn();
        accountStateService.clearFavoritesAndCart();
        authService.openHomePageAndEnsureUserLoggedIn();
    }

    @AfterEach
    public void tearDown() {
        try {
            LOGGER.info(DATA_CLEANUP_LOG_MESSAGE);
            accountStateService.clearFavoritesAndCart();
        } finally {
            LOGGER.info(BROWSER_CLOSING_LOG_MESSAGE);
            Selenide.closeWebDriver();
        }
    }
}
