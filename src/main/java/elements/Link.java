package elements;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.$x;

/** Ссылка, переход по которой выполняется пользовательским кликом. */
public class Link extends ClickableElement {

    private static final String TARGET_ATTRIBUTE = "target";
    private static final String CLICK_EVENT_NAME = "click";
    private static final String SAME_TAB_TARGET = "_self";
    private static final Duration NAVIGATION_WAIT_TIMEOUT = Duration.ofSeconds(3);
    private static final String SET_SAME_TAB_TARGET_SCRIPT =
            "arguments[0].addEventListener(arguments[1],"
                    + " event => event.stopImmediatePropagation(),"
                    + " {capture: true, once: true});"
                    + " arguments[0].setAttribute(arguments[2], arguments[3])";

    protected Link(SelenideElement element) {
        super(element);
    }

    /** Кликает по ссылке в текущей вкладке. */
    @Override
    public void click() {
        SelenideElement interactableLink = waitUntilInteractable();
        // Ozon открывает часть ссылок обработчиком JavaScript даже без target="_blank".
        // Отключается только этот обработчик; переход остаётся результатом нативного клика.
        executeJavaScript(SET_SAME_TAB_TARGET_SCRIPT,
                interactableLink, CLICK_EVENT_NAME, TARGET_ATTRIBUTE, SAME_TAB_TARGET);
        interactableLink.click();
    }

    /** Кликает и ожидает фактического изменения адреса текущей вкладки. */
    public void clickAndWaitForNavigation() {
        String locationBeforeClick = WebDriverRunner.url();
        click();
        new WebDriverWait(WebDriverRunner.getWebDriver(), NAVIGATION_WAIT_TIMEOUT)
                .until(webDriver -> !Objects.equals(
                        locationBeforeClick,
                        webDriver.getCurrentUrl()));
    }

    public static Link byXPath(String xpathExpression) {
        return new Link($x(xpathExpression));
    }

    public static Link from(SelenideElement element) {
        return new Link(element);
    }
}
