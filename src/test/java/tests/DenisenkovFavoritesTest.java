package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.FavoritesPage;
import pages.MainPage;

import static org.assertj.core.api.Assertions.assertThat;

public class DenisenkovFavoritesTest extends BaseTest {

    @Test
    @DisplayName("4. Удаление товара из избранного")
    public void removeFromFavorites() {
        MainPage main = new MainPage().openMain();
        FavoritesPage favorites = main.openFavorites();
        String productName = favorites.getFirstProductName();
        int headerBefore = main.getHeaderFavoritesCount();
        int titleBefore = favorites.getHeaderCounter();

        favorites.removeByName(productName);

        assertThat(favorites.isHeartActiveByName(productName)).as("Сердце стало серым").isFalse();
        assertThat(main.getHeaderFavoritesCount()).as("Счётчик в шапке уменьшился")
                .isEqualTo(headerBefore - 1);
        assertThat(favorites.getHeaderCounter()).as("Счётчик возле заголовка уменьшился")
                .isEqualTo(titleBefore - 1);

        favorites.refreshPage();
        assertThat(favorites.hasProduct(productName)).as("После обновления товара нет").isFalse();
    }

    @Test
    @DisplayName("8. Добавление товара из избранного в корзину")
    public void addToCartFromFavorites() {
        MainPage main = new MainPage().openMain();
        FavoritesPage favorites = main.openFavorites();
        String productName = favorites.getFirstAvailableForCartProductName();
        favorites.addToCartByName(productName);
        assertThat(favorites.isInCartStateByName(productName)).as("Появился контрол количества").isTrue();

        CartPage cart = main.openCart();
        assertThat(cart.hasProduct(productName)).as("Товар находится в корзине").isTrue();
    }
}
