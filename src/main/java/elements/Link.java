package elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.$x;

/** Ссылка, переход по которой выполняется пользовательским кликом. */
public class Link extends ClickableElement {

    private static final String TARGET_ATTRIBUTE = "target";
    private static final String CLICK_EVENT_NAME = "click";
    private static final String NEW_TAB_TARGET = "_blank";
    private static final String SAME_TAB_TARGET = "_self";
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
        SelenideElement visibleLink = waitUntilVisible();
        if (NEW_TAB_TARGET.equals(visibleLink.getDomAttribute(TARGET_ATTRIBUTE))) {
            // ChromeDriver 150 зависает на API вкладок. Одноразово отключается
            // только обработчик Ozon, открывающий вкладку; нативный переход сохраняется.
            executeJavaScript(SET_SAME_TAB_TARGET_SCRIPT,
                    visibleLink, CLICK_EVENT_NAME, TARGET_ATTRIBUTE, SAME_TAB_TARGET);
        }
        super.click();
    }

    public static Link byXPath(String xpathExpression) {
        return new Link($x(xpathExpression));
    }

    public static Link from(SelenideElement element) {
        return new Link(element);
    }
}
