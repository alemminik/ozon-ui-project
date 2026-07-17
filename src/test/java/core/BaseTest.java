package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Настраивает браузер, авторизацию и завершение сессии для UI-тестов. */
@ExtendWith(TestExecutionLogger.class)
public abstract class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    private static final String BROWSER_NAME = "chrome";
    private static final String BROWSER_SIZE = "1920x1080";
    private static final String PAGE_LOAD_STRATEGY = "none";
    private static final String AUTHORIZATION_CONFIRMED_LOG_MESSAGE = "Авторизация подтверждена";
    private static final String TEST_START_LOG_MESSAGE = "Тест %s";
    private static final String BROWSER_CLOSED_LOG_MESSAGE = "Браузер закрыт";
    private static final String TEST_STEP_LOG_MESSAGE = "%d. %s";
    private static final String STEP_SCREENSHOT_NAME_TEMPLATE = "%s-%s-step-%02d-before";
    private static final String STEP_SCREENSHOT_FAILURE_MESSAGE =
            "Скриншот перед шагом %d не сохранён: %s";
    private static final String SCREENSHOT_SERIES_FILE_NAME_TEMPLATE =
            "%s-%s-step-XX-before.png";
    private static final String TEST_RUN_TIMESTAMP_PATTERN = "yyyyMMdd-HHmmss-SSS";
    private static final Duration ELEMENT_WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final boolean HEADLESS_MODE_ENABLED = false;
    private static final DateTimeFormatter TEST_RUN_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern(TEST_RUN_TIMESTAMP_PATTERN);

    private final AuthService authService = new AuthService();
    protected final AccountStateService accountStateService = new AccountStateService();
    private String currentTestMethodName;
    private String currentTestRunTimestamp;
    private int currentStepNumber;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        currentTestMethodName = testInfo.getTestMethod()
                .orElseThrow()
                .getName();
        currentTestRunTimestamp = LocalDateTime.now().format(TEST_RUN_TIMESTAMP_FORMAT);
        currentStepNumber = 0;
        Configuration.browser = BROWSER_NAME;
        Configuration.browserSize = BROWSER_SIZE;
        Configuration.pageLoadStrategy = PAGE_LOAD_STRATEGY;
        Configuration.timeout = ELEMENT_WAIT_TIMEOUT.toMillis();
        Configuration.headless = HEADLESS_MODE_ENABLED;
        Configuration.browserCapabilities = AuthService.createBrowserOptions();
        TestExecutionLogger.startScreenshotSeries(createScreenshotSeriesUri());

        authService.openHomePageAndEnsureUserLoggedIn();
        accountStateService.clearFavoritesAndCart();
        authService.openHomePageAndEnsureUserLoggedIn();
        LOGGER.info(AUTHORIZATION_CONFIRMED_LOG_MESSAGE);
        LOGGER.info(String.format(TEST_START_LOG_MESSAGE, testInfo.getDisplayName()));
    }

    @AfterEach
    public void tearDown() {
        try {
            accountStateService.clearFavoritesAndCart();
        } finally {
            Selenide.closeWebDriver();
            LOGGER.info(BROWSER_CLOSED_LOG_MESSAGE);
        }
    }

    /** Сохраняет состояние страницы и записывает следующий пользовательский шаг. */
    protected void logStep(String stepDescription) {
        currentStepNumber++;
        LOGGER.info(String.format(
                TEST_STEP_LOG_MESSAGE,
                currentStepNumber,
                stepDescription));
        saveScreenshotBeforeCurrentStep();
    }

    private void saveScreenshotBeforeCurrentStep() {
        String screenshotName = String.format(
                STEP_SCREENSHOT_NAME_TEMPLATE,
                currentTestMethodName,
                currentTestRunTimestamp,
                currentStepNumber);
        try {
            Selenide.screenshot(screenshotName);
            TestExecutionLogger.recordSavedScreenshot();
        } catch (RuntimeException screenshotFailure) {
            LOGGER.warn(String.format(
                    STEP_SCREENSHOT_FAILURE_MESSAGE,
                    currentStepNumber,
                    screenshotFailure.getMessage()));
        }
    }

    private String createScreenshotSeriesUri() {
        String screenshotSeriesFileName = String.format(
                SCREENSHOT_SERIES_FILE_NAME_TEMPLATE,
                currentTestMethodName,
                currentTestRunTimestamp);
        return new File(Configuration.reportsFolder, screenshotSeriesFileName)
                .toURI()
                .toString();
    }
}
