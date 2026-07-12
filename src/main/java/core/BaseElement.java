package core;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Базовый класс всех элементов страницы.
 * Реализует общий для всех элементов функционал (проверка отображения, клик, получение текста).
 * Конкретные элементы (кнопка, поле ввода, иконка и т.д.) наследуются от него.
 */
public abstract class BaseElement {

    protected static final Duration WAIT = Duration.ofSeconds(15);

    protected final SelenideElement element;

    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    /** Проверка, отображается ли элемент на странице. */
    public boolean isDisplayed() {
        try {
            return element.shouldBe(visible, WAIT).isDisplayed();
        } catch (Throwable e) {
            return false;
        }
    }

    /** Текст элемента. */
    public String getText() {
        return element.shouldBe(visible, WAIT).getText();
    }

    /** Клик по элементу (с предварительной прокруткой к нему). */
    protected void click() {
        element.shouldBe(visible, WAIT);
        executeJavaScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'})", element);
        element.shouldBe(visible, WAIT).click();
    }
}
