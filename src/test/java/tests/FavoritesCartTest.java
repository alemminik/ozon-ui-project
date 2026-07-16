package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.FavoritesPage;
import pages.MainPage;

import static core.TestProductQueries.PRIMARY_ELECTRONICS;
import static org.assertj.core.api.Assertions.assertThat;

/** Проверка добавления избранного товара в корзину. */
public class FavoritesCartTest extends BaseTest {

    @Test
    @DisplayName("8. Добавление товара из избранного в корзину")
    public void shouldAddFavoriteProductToCart() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        String productName = favoritesPage.getFirstAvailableFavoriteProductNameForCart();

        favoritesPage.addFavoriteProductToCart(productName);
        assertThat(favoritesPage.isProductInCart(productName))
                .as("Появился контрол количества").isTrue();

        CartPage cartPage = mainPage.openCart();
        assertThat(cartPage.isProductDisplayed(productName))
                .as("Товар находится в корзине").isTrue();
    }
}
