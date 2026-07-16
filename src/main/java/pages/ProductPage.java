package pages;

import core.BasePage;
import elements.HeartIcon;
import elements.TextElement;

/** Страница товара: название и управление состоянием избранного. */
public class ProductPage extends BasePage {

    private static final String PRODUCT_TITLE_XPATH =
            "//div[@data-widget='webProductHeading']//h1";
    private static final String PRODUCT_HEART_XPATH =
            "(//div[@data-widget='webAddToFavorite']//button"
                    + "[@aria-label='Добавить в избранное'"
                    + " or @aria-label='Удалить из избранного'"
                    + " or @aria-label='Убрать из избранного'])[1]";
    private final TextElement productTitle = TextElement.byXPath(PRODUCT_TITLE_XPATH);
    private final HeartIcon favoriteHeart = HeartIcon.byXPath(PRODUCT_HEART_XPATH);

    public void addToFavorites() {
        favoriteHeart.toggleFavoriteState();
    }

    public boolean isProductInFavorites() {
        return favoriteHeart.isActive();
    }

    public String getProductName() {
        return productTitle.getText();
    }

    ProductPage waitUntilLoaded() {
        waitUntilDocumentReady();
        productTitle.getText();
        return this;
    }
}
