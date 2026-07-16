package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.value;

/** Поле ввода данных. */
public class Input extends BaseElement {

    private final Button clearButton;

    private Input(SelenideElement element, Button clearButton) {
        super(element);
        this.clearButton = clearButton;
    }

    /** Заменяет значение поля и отправляет форму нажатием Enter. */
    public void fillAndSubmit(String newValue) {
        replaceCurrentValue(newValue).pressEnter();
    }

    private SelenideElement replaceCurrentValue(String newValue) {
        SelenideElement input = waitUntilVisible();
        if (!input.getValue().isEmpty()) {
            clearButton.click();
        }
        input.shouldBe(empty, ELEMENT_WAIT_TIMEOUT);
        return input.setValue(newValue)
                .shouldHave(value(newValue), ELEMENT_WAIT_TIMEOUT);
    }

    public static Input from(SelenideElement element, Button clearButton) {
        return new Input(element, clearButton);
    }
}
