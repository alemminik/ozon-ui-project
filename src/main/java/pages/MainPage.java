package pages;

import com.codeborne.selenide.ex.UIAssertionError;
import core.BasePage;
import elements.Header;

/** Главная страница и доступные на всех страницах действия общей шапки. */
public class MainPage extends BasePage {

    private final Header header = new Header();

    public SearchResultsPage search(String searchQuery) {
        waitUntilDocumentReady();
        header.submitSearchQuery(searchQuery);
        return new SearchResultsPage().waitUntilLoaded();
    }

    public FavoritesPage openFavorites() {
        waitUntilDocumentReady();
        FavoritesPage favoritesPage = new FavoritesPage();
        if (favoritesPage.isLoaded()) {
            return favoritesPage;
        }
        try {
            header.clickFavorites();
            return favoritesPage.waitUntilLoaded();
        } catch (UIAssertionError firstNavigationTimeout) {
            header.clickFavorites();
            return new FavoritesPage().waitUntilLoaded();
        }
    }

    public CartPage openCart() {
        waitUntilDocumentReady();
        CartPage cartPage = new CartPage();
        if (cartPage.isLoaded()) {
            return cartPage;
        }
        try {
            header.clickCart();
            return cartPage.waitUntilLoaded();
        } catch (UIAssertionError firstNavigationTimeout) {
            header.clickCart();
            return new CartPage().waitUntilLoaded();
        }
    }

    public int getHeaderFavoritesCount() {
        return header.getFavoritesCount();
    }
}
