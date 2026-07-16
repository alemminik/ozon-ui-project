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

    private static final String TEST_DISPLAY_NAME =
            "8. Добавление товара из избранного в корзину";
    private static final String QUANTITY_CONTROL_ASSERTION = "Появился контрол количества";
    private static final String PRODUCT_IN_CART_ASSERTION = "Товар находится в корзине";

    @Test
    @DisplayName(TEST_DISPLAY_NAME)
    public void shouldAddFavoriteProductToCart() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        String productName = favoritesPage.getFirstAvailableFavoriteProductNameForCart();

        favoritesPage.addFavoriteProductToCart(productName);
        assertThat(favoritesPage.isProductInCart(productName))
                .as(QUANTITY_CONTROL_ASSERTION).isTrue();

        CartPage cartPage = mainPage.openCart();
        assertThat(cartPage.isProductDisplayed(productName))
                .as(PRODUCT_IN_CART_ASSERTION).isTrue();
    }
}
