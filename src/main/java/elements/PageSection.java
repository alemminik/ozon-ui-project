package elements;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ScrollIntoViewOptions;
import core.BaseElement;

import static com.codeborne.selenide.ScrollIntoViewOptions.Block.center;
import static com.codeborne.selenide.ScrollIntoViewOptions.Block.end;
import static com.codeborne.selenide.ScrollIntoViewOptions.Inline.nearest;

/** Область страницы, к которой пользователь может прокрутить документ. */
public class PageSection extends BaseElement {

    private static final ScrollIntoViewOptions CENTERED_SCROLL_OPTIONS =
            ScrollIntoViewOptions.instant().block(center).inline(nearest);
    private static final ScrollIntoViewOptions BOTTOM_EDGE_SCROLL_OPTIONS =
            ScrollIntoViewOptions.instant().block(end).inline(nearest);

    private PageSection(SelenideElement element) {
        super(element);
    }

    public void scrollIntoView() {
        waitUntilVisible().scrollIntoView(CENTERED_SCROLL_OPTIONS);
    }

    public void scrollToBottomEdge() {
        waitUntilVisible().scrollIntoView(BOTTOM_EDGE_SCROLL_OPTIONS);
    }

    public static PageSection from(SelenideElement element) {
        return new PageSection(element);
    }
}
