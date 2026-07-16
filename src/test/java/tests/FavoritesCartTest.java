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
    private static final String STEP_1 = "Открыть раздел «Избранное».";
    private static final String STEP_2 =
            "Считать название первого товара, доступного для добавления в корзину.";
    private static final String STEP_3 =
            "Нажать кнопку добавления в корзину в карточке этого товара.";
    private static final String STEP_4 =
            "Проверить появление в карточке элемента управления количеством товара.";
    private static final String STEP_5 = "Перейти в раздел «Корзина».";
    private static final String STEP_6 =
            "Проверить наличие товара с сохранённым названием.";
    private static final String QUANTITY_CONTROL_ASSERTION = "Появился контрол количества";
    private static final String PRODUCT_IN_CART_ASSERTION = "Товар находится в корзине";

    @Test
    @DisplayName(TEST_DISPLAY_NAME)
    public void shouldAddFavoriteProductToCart() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        logStep(STEP_1);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        logStep(STEP_2);
        String productName = favoritesPage.getFirstAvailableFavoriteProductNameForCart();

        logStep(STEP_3);
        favoritesPage.addFavoriteProductToCart(productName);
        logStep(STEP_4);
        assertThat(favoritesPage.isProductInCart(productName))
                .as(QUANTITY_CONTROL_ASSERTION).isTrue();

        logStep(STEP_5);
        CartPage cartPage = mainPage.openCart();
        logStep(STEP_6);
        assertThat(cartPage.isProductDisplayed(productName))
                .as(PRODUCT_IN_CART_ASSERTION).isTrue();
    }
}
