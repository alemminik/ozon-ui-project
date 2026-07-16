package elements;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

/** Кнопка, доступная для пользовательского клика. */
public class Button extends ClickableElement {

    private Button(SelenideElement element) {
        super(element);
    }

    public static Button byXPath(String xpathExpression) {
        return new Button($x(xpathExpression));
    }

    public static Button from(SelenideElement element) {
        return new Button(element);
    }
}
