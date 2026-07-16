package elements;

import core.BaseElement;

import static com.codeborne.selenide.Selenide.$x;

/** Общая шапка Ozon: поиск, избранное, корзина и счётчик избранного. */
public class Header extends BaseElement {

    private static final String HEADER_ROOT_XPATH =
            "(//header | //div[@data-widget='header'])[1]";
    private static final String SEARCH_INPUT_RELATIVE_XPATH =
            ".//div[@data-widget='searchBarDesktop']//input[@name='text']";
    private static final String SEARCH_CLEAR_BUTTON_RELATIVE_XPATH =
            SEARCH_INPUT_RELATIVE_XPATH + "/following-sibling::button[1]";
    private static final String FAVORITES_LINK_RELATIVE_XPATH =
            ".//a[@data-widget='favoriteCounter']";
    private static final String CART_LINK_RELATIVE_XPATH =
            ".//a[@data-widget='headerIcon' and contains(@href, '/cart')]";
    private static final String FAVORITES_COUNTER_RELATIVE_XPATH =
            FAVORITES_LINK_RELATIVE_XPATH + "/div[normalize-space() and not(.//span)]";

    private final Input searchInput;
    private final Link favoritesLink;
    private final Link cartLink;
    private final Counter favoritesCounter;

    public Header() {
        super($x(HEADER_ROOT_XPATH));
        searchInput = Input.from(
                element.$x(SEARCH_INPUT_RELATIVE_XPATH),
                Button.from(element.$x(SEARCH_CLEAR_BUTTON_RELATIVE_XPATH)));
        favoritesLink = Link.from(element.$x(FAVORITES_LINK_RELATIVE_XPATH));
        cartLink = Link.from(element.$x(CART_LINK_RELATIVE_XPATH));
        favoritesCounter = Counter.from(element.$x(FAVORITES_COUNTER_RELATIVE_XPATH));
    }

    public void submitSearchQuery(String searchQuery) {
        searchInput.fillAndSubmit(searchQuery);
    }

    public void clickFavorites() {
        favoritesLink.click();
    }

    public void clickCart() {
        cartLink.click();
    }

    public int getFavoritesCount() {
        waitUntilVisible();
        return favoritesCounter.getValue();
    }
}
