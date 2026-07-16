package core;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Фиксирует результат выполнения каждого UI-теста. */
public class TestExecutionLogger implements AfterTestExecutionCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutionLogger.class);
    private static final String SCREENSHOTS_SAVED_MESSAGE =
            "Скриншоты шагов сохранены ({}): {}";
    private static final String TEST_PASSED_MESSAGE = "Тест {} успешно завершён";
    private static final String TEST_FAILED_MESSAGE = "Тест {} завершён с ошибкой: {}";
    private static final ThreadLocal<ScreenshotSeries> SCREENSHOT_SERIES = new ThreadLocal<>();

    @Override
    public void afterTestExecution(ExtensionContext context) {
        String testDisplayName = context.getDisplayName();
        logScreenshotSeries();
        context.getExecutionException().ifPresentOrElse(
                testFailure -> LOGGER.error(
                        TEST_FAILED_MESSAGE, testDisplayName, testFailure.getMessage()),
                () -> LOGGER.info(TEST_PASSED_MESSAGE, testDisplayName));
        SCREENSHOT_SERIES.remove();
    }

    static void startScreenshotSeries(String screenshotPathPattern) {
        SCREENSHOT_SERIES.set(new ScreenshotSeries(screenshotPathPattern));
    }

    static void recordSavedScreenshot() {
        ScreenshotSeries screenshotSeries = SCREENSHOT_SERIES.get();
        if (screenshotSeries != null) {
            screenshotSeries.incrementSavedScreenshotCount();
        }
    }

    private void logScreenshotSeries() {
        ScreenshotSeries screenshotSeries = SCREENSHOT_SERIES.get();
        if (screenshotSeries != null && screenshotSeries.savedScreenshotCount > 0) {
            LOGGER.info(
                    SCREENSHOTS_SAVED_MESSAGE,
                    screenshotSeries.savedScreenshotCount,
                    screenshotSeries.screenshotPathPattern);
        }
    }

    private static final class ScreenshotSeries {

        private final String screenshotPathPattern;
        private int savedScreenshotCount;

        private ScreenshotSeries(String screenshotPathPattern) {
            this.screenshotPathPattern = screenshotPathPattern;
        }

        private void incrementSavedScreenshotCount() {
            savedScreenshotCount++;
        }
    }
}
