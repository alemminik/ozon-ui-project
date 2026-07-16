package elements;

import com.codeborne.selenide.ElementsCollection;
import core.BaseElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

/** Блок рекомендаций с динамической подгрузкой карточек. */
public class RecommendationGrid extends BaseElement {

    private static final String PRODUCT_CARD_RELATIVE_XPATH =
            "//div[contains(concat(' ', normalize-space(@class), ' '), ' tile-root ')]";

    private final String rootXPath;

    private RecommendationGrid(String rootXPath) {
        super($x(rootXPath));
        this.rootXPath = rootXPath;
    }

    public void scrollIntoView() {
        PageSection.from(waitUntilVisible()).scrollIntoView();
    }

    public int getProductCount() {
        waitUntilVisible();
        return getProductCards().size();
    }

    public void loadMoreProducts() {
        ElementsCollection productCards = getProductCards();
        int productCountBeforeScroll = productCards.size();
        PageSection.from(productCards.last()).scrollToBottomEdge();
        productCards.shouldHave(
                sizeGreaterThan(productCountBeforeScroll),
                ELEMENT_WAIT_TIMEOUT);
    }

    private ElementsCollection getProductCards() {
        return $$x(rootXPath + PRODUCT_CARD_RELATIVE_XPATH);
    }

    public static RecommendationGrid byXPath(String rootXPath) {
        return new RecommendationGrid(rootXPath);
    }
}
