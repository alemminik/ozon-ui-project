package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Selenide.$x;

/** Элемент, для которого требуется только проверка отображения. */
public class VisibleElement extends BaseElement {

    private VisibleElement(SelenideElement element) {
        super(element);
    }

    public void waitUntilDisplayed() {
        waitUntilVisible();
    }

    public static VisibleElement byXPath(String xpathExpression) {
        return new VisibleElement($x(xpathExpression));
    }
}
