package elements;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;

/** Набор однотипных кнопок, среди которых действие выполняется с видимой кнопкой. */
public class ButtonCollection {

    private final ElementsCollection buttons;

    private ButtonCollection(ElementsCollection buttons) {
        this.buttons = buttons;
    }

    public void clickFirstVisible() {
        Button.from(buttons.findBy(visible)).click();
    }

    public static ButtonCollection byXPath(String xpathExpression) {
        return new ButtonCollection($$x(xpathExpression));
    }
}
