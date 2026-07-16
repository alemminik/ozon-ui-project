package core;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/** Общая основа Page Object. */
public abstract class BasePage {

    private static final String DOCUMENT_READY_STATE_SCRIPT = "return document.readyState";
    private static final String COMPLETE_DOCUMENT_READY_STATE = "complete";
    private static final Duration DOCUMENT_READY_TIMEOUT = Duration.ofSeconds(30);

    /** Обновляет текущую страницу и возвращает её Page Object. */
    public BasePage refreshPage() {
        Selenide.refresh();
        waitUntilDocumentReady();
        return this;
    }

    /** Ожидает завершения загрузки документа без фиксированной паузы. */
    protected void waitUntilDocumentReady() {
        new WebDriverWait(getWebDriver(), DOCUMENT_READY_TIMEOUT)
                .until(webDriver -> {
                    try {
                        return COMPLETE_DOCUMENT_READY_STATE.equals(
                                ((JavascriptExecutor) webDriver)
                                        .executeScript(DOCUMENT_READY_STATE_SCRIPT));
                    } catch (WebDriverException documentIsChanging) {
                        // SPA-навигация Ozon может заменить frame во время проверки.
                        return false;
                    }
                });
    }
}
