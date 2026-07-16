package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Selenide.$x;

/** Текстовый элемент страницы. */
public class TextElement extends BaseElement implements TextReadable {

    private TextElement(SelenideElement element) {
        super(element);
    }

    @Override
    public String getText() {
        return waitUntilVisible().getText().trim();
    }

    public static TextElement byXPath(String xpathExpression) {
        return new TextElement($x(xpathExpression));
    }

    public static TextElement from(SelenideElement element) {
        return new TextElement(element);
    }
}
