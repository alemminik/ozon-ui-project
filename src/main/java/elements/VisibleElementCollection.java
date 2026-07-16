package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ex.UIAssertionError;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;

/** Набор элементов, в котором проверяется видимость элемента с заданным текстом. */
public class VisibleElementCollection {

    private final ElementsCollection elements;

    private VisibleElementCollection(ElementsCollection elements) {
        this.elements = elements;
    }

    public boolean isElementWithTextDisplayed(String expectedText) {
        try {
            elements.findBy(text(expectedText)).shouldBe(visible);
            return true;
        } catch (UIAssertionError expectedTimeout) {
            return false;
        }
    }

    public static VisibleElementCollection byXPath(String xpathExpression) {
        return new VisibleElementCollection($$x(xpathExpression));
    }
}
