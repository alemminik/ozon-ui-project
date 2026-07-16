package core;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.UIAssertionError;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;

/** Общая основа элементов страницы с ожиданием видимости. */
public abstract class BaseElement {

    protected static final Duration ELEMENT_WAIT_TIMEOUT = Duration.ofSeconds(15);
    protected static final Duration ELEMENT_ABSENCE_TIMEOUT = Duration.ofSeconds(3);

    protected final SelenideElement element;

    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    /** Возвращает признак отображения элемента после динамического ожидания. */
    public boolean isDisplayed() {
        try {
            waitUntilVisible();
            return true;
        } catch (UIAssertionError expectedTimeout) {
            return false;
        }
    }

    /** Проверяет наличие элемента в текущем DOM без ожидания. */
    public boolean isPresent() {
        return element.exists();
    }

    /** Ожидает исчезновение элемента в пределах короткого отрицательного таймаута. */
    public boolean isAbsent() {
        try {
            element.should(disappear, ELEMENT_ABSENCE_TIMEOUT);
            return true;
        } catch (UIAssertionError expectedTimeout) {
            return false;
        }
    }

    protected SelenideElement waitUntilVisible() {
        return element.shouldBe(visible, ELEMENT_WAIT_TIMEOUT);
    }
}
