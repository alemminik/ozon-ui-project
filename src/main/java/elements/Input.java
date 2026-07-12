package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Базисный элемент «поле ввода».
 * Умеет то, что пользователь может делать с полем: заполнять его значением.
 */
public class Input extends BaseElement {

    private Input(SelenideElement element) {
        super(element);
    }

    /** Поле ввода по значению атрибута placeholder. */
    public static Input byPlaceholder(String placeholder) {
        return new Input($x(String.format("//input[@placeholder='%s']", placeholder)));
    }

    /** Поле ввода по произвольному xpath. */
    public static Input byXpath(String xpath) {
        return new Input($x(xpath));
    }

    /** Заполнение поля значением (с предварительной очисткой) и нажатие Enter. */
    public void fill(String value) {
        replaceValue(value).pressEnter();
    }

    /** Заполнение поля без нажатия Enter. */
    public void type(String value) {
        replaceValue(value);
    }

    private SelenideElement replaceValue(String value) {
        SelenideElement input = element.shouldBe(visible, WAIT);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.BACK_SPACE);
        return input.setValue(value).shouldHave(value(value), WAIT);
    }
}
