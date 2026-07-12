package pages;

import core.BasePage;
import elements.HeartIcon;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Страница результатов поиска.
 * Позволяет открыть товар, нажать иконку сердца у карточки и узнать состояние иконки.
 */
public class SearchResultsPage extends BasePage {

    /** Открыть товар с указанным названием. Возвращает страницу товара. */
    public ProductPage openProductByName(String name) {
        String card = String.format(Locators.SEARCH_CARD_BY_NAME, name);
        String link = $x(card).exists()
                ? String.format(Locators.SEARCH_PRODUCT_LINK_BY_NAME, name)
                : Locators.SEARCH_FIRST_PRODUCT_LINK;
        openLinkInCurrentTab(link);
        return new ProductPage();
    }

    /** Открыть первый товар из результатов поиска. */
    public ProductPage openFirstProduct() {
        openLinkInCurrentTab(Locators.SEARCH_FIRST_PRODUCT_LINK);
        return new ProductPage();
    }

    /** Нажать иконку сердца у карточки товара с указанным названием. */
    public void clickHeartByName(String name) {
        heartByName(name).toggle();
    }

    /** Нажать иконку сердца у первого товара в выдаче. */
    public void clickFirstHeart() {
        HeartIcon.byXpath(Locators.SEARCH_FIRST_CARD_HEART).toggle();
    }

    public void addFirstToFavorites() {
        HeartIcon heart = HeartIcon.byXpath(Locators.SEARCH_FIRST_CARD_HEART);
        if (!heart.isActive()) {
            heart.toggle();
        }
        heart.waitForState(true);
    }

    public String getFirstProductName() {
        return $x(Locators.SEARCH_FIRST_PRODUCT_NAME).getText().trim();
    }

    public void ensureFirstHeartActive(boolean active) {
        HeartIcon heart = HeartIcon.byXpath(Locators.SEARCH_FIRST_CARD_HEART);
        if (heart.isActive() != active) {
            heart.toggle();
            heart.waitForState(active);
        }
    }

    /** Активна ли иконка сердца у карточки товара с указанным названием. */
    public boolean isHeartActiveByName(String name) {
        return heartByName(name).isActive();
    }

    /** Активна ли иконка сердца у первого товара. */
    public boolean isFirstHeartActive() {
        return HeartIcon.byXpath(Locators.SEARCH_FIRST_CARD_HEART).isActive();
    }

    private HeartIcon heartByName(String name) {
        String card = String.format(Locators.SEARCH_CARD_BY_NAME, name);
        String heart = $x(card).exists()
                ? String.format(Locators.SEARCH_CARD_HEART_BY_NAME, name)
                : Locators.SEARCH_FIRST_CARD_HEART;
        return HeartIcon.byXpath(heart);
    }
}
