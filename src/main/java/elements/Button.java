package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Базисный элемент «кнопка».
 * Умеет то, что пользователь может делать с кнопкой: нажимать и проверять отображение.
 */
public class Button extends BaseElement {

    private Button(SelenideElement element) {
        super(element);
    }

    /** Кнопка по видимому тексту. */
    public static Button byText(String text) {
        return new Button($x(String.format("//button[contains(., '%s')]", text)));
    }

    /** Кнопка по произвольному xpath. */
    public static Button byXpath(String xpath) {
        return new Button($x(xpath));
    }

    /** Нажатие на кнопку. */
    public void press() {
        click();
    }
}
