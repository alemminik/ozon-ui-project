package pages;

import core.BasePage;
import elements.Input;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

/** Общие элементы шапки Ozon: поиск, избранное, корзина и счётчик избранного. */
public class MainPage extends BasePage {

    public static final String URL = "https://www.ozon.ru";

    private final Input searchInput = Input.byXpath(Locators.SEARCH_INPUT);

    public MainPage openMain() {
        open(URL);
        return this;
    }

    public SearchResultsPage search(String query) {
        searchInput.fill(query);
        return new SearchResultsPage();
    }

    public FavoritesPage openFavorites() {
        openLinkInCurrentTab(Locators.HEADER_FAVORITES_LINK);
        return new FavoritesPage();
    }

    public CartPage openCart() {
        openLinkInCurrentTab(Locators.HEADER_CART_LINK);
        return new CartPage();
    }

    public int getHeaderFavoritesCount() {
        return CounterReader.read(Locators.HEADER_FAVORITES_COUNTER);
    }

    static class CounterReader {
        static int read(String xpath) {
            try {
                String text = $x(xpath).getText().replaceAll("\\D+", "");
                return text.isEmpty() ? 0 : Integer.parseInt(text);
            } catch (Throwable e) {
                return 0;
            }
        }
    }
}
