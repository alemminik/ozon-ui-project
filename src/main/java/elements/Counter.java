package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Selenide.$x;

/** Числовой счётчик, отображаемый на странице. */
public class Counter extends BaseElement {

    private static final String NON_DIGIT_CHARACTERS_PATTERN = "\\D+";
    private static final String EMPTY_TEXT = "";
    private static final int EMPTY_COUNTER_VALUE = 0;

    private Counter(SelenideElement element) {
        super(element);
    }

    public int getValue() {
        // Бейдж с нулевым значением отсутствует в DOM, поэтому ждём его возможное появление.
        if (!isDisplayed()) {
            return EMPTY_COUNTER_VALUE;
        }
        String counterText = waitUntilVisible().getText()
                .replaceAll(NON_DIGIT_CHARACTERS_PATTERN, EMPTY_TEXT);
        return counterText.isEmpty()
                ? EMPTY_COUNTER_VALUE
                : Integer.parseInt(counterText);
    }

    public static Counter byXPath(String xpathExpression) {
        return new Counter($x(xpathExpression));
    }

    public static Counter from(SelenideElement element) {
        return new Counter(element);
    }
}
