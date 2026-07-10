package core;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseElement {
    protected final SelenideElement element;

    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    public String getText() {
        return element.getText();
    }

    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    protected void click() {
        element.scrollIntoView(true).click();
    }
}
