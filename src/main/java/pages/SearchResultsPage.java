package pages;

import core.BasePage;
import elements.HeartIcon;
import elements.Link;
import elements.TextElement;

/** Результаты поиска товаров. */
public class SearchResultsPage extends BasePage {

    private static final String SEARCH_GRID_XPATH =
            "(//div[@data-widget='tileGridDesktop'])[1]";
    private static final String FIRST_PRODUCT_CARD_XPATH =
            "(" + SEARCH_GRID_XPATH
                    + "//div[contains(concat(' ', normalize-space(@class), ' '),"
                    + " ' tile-root ')])[1]";
    private static final String FIRST_PRODUCT_LINK_XPATH = FIRST_PRODUCT_CARD_XPATH
            + "//a[contains(@href, '/product/')][normalize-space()][1]";
    private static final String FIRST_PRODUCT_NAME_XPATH =
            "(" + FIRST_PRODUCT_CARD_XPATH
                    + "//a[contains(@href, '/product/')][normalize-space()])[last()]";
    private static final String FIRST_PRODUCT_HEART_XPATH =
            "(" + FIRST_PRODUCT_CARD_XPATH
                    + "//button[.//*[name()='svg'][@width='24' and @height='24']"
                    + "/*[name()='path' and (@fill='white'"
                    + " or translate(@fill, 'abcdef', 'ABCDEF')='#F8104B')]])[1]";

    private final TextElement firstProductName = TextElement.byXPath(FIRST_PRODUCT_NAME_XPATH);
    private final Link firstProductLink = Link.byXPath(FIRST_PRODUCT_LINK_XPATH);
    private final HeartIcon firstProductHeart = HeartIcon.byXPath(FIRST_PRODUCT_HEART_XPATH);

    public ProductPage openFirstSearchResultProduct() {
        firstProductLink.click();
        return new ProductPage().waitUntilLoaded();
    }

    public void addFirstSearchResultToFavorites() {
        firstProductHeart.toggleFavoriteState();
    }

    public void toggleFirstSearchResultFavoriteState() {
        firstProductHeart.toggleFavoriteState();
    }

    public String getFirstSearchResultProductName() {
        return firstProductName.getText();
    }

    public boolean isFirstSearchResultInFavorites() {
        return firstProductHeart.isActive();
    }

    SearchResultsPage waitUntilLoaded() {
        firstProductName.getText();
        return this;
    }

}
