package elements;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ScrollIntoViewOptions;
import core.BaseElement;

import static com.codeborne.selenide.ScrollIntoViewOptions.Block.center;
import static com.codeborne.selenide.ScrollIntoViewOptions.Inline.nearest;

/** Основа элементов, по которым пользователь может кликнуть. */
public abstract class ClickableElement extends BaseElement implements Clickable {

    private static final ScrollIntoViewOptions CENTERED_SCROLL_OPTIONS =
            ScrollIntoViewOptions.instant().block(center).inline(nearest);

    protected ClickableElement(SelenideElement element) {
        super(element);
    }

    @Override
    public void click() {
        waitUntilVisible().scrollIntoView(CENTERED_SCROLL_OPTIONS).click();
    }
}
