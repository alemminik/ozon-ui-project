package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Condition.selected;

/** Флажок, состояние которого пользователь может выбрать кликом. */
public class Checkbox extends BaseElement {

    private static final String CLICKABLE_LABEL_XPATH = "ancestor::label[1]";

    private Checkbox(SelenideElement element) {
        super(element);
    }

    public void select() {
        SelenideElement checkbox = waitUntilVisible();
        if (!checkbox.isSelected()) {
            checkbox.$x(CLICKABLE_LABEL_XPATH).click();
        }
        checkbox.shouldBe(selected, ELEMENT_WAIT_TIMEOUT);
    }

    public static Checkbox from(SelenideElement element) {
        return new Checkbox(element);
    }
}
