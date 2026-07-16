package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.FavoritesPage;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultsPage;

import java.util.List;

import static core.TestProductQueries.PRIMARY_ELECTRONICS;
import static core.TestProductQueries.SECOND_ELECTRONICS;
import static core.TestProductQueries.THIRD_ELECTRONICS;
import static org.assertj.core.api.Assertions.assertThat;

/** Проверки добавления, удаления, порядка и синхронизации избранных товаров. */
public class FavoritesProductManagementTest extends BaseTest {

    private static final int FAVORITES_COUNT_DECREMENT = 1;

    @Test
    @DisplayName("2. Добавление в избранное со страницы товара")
    public void shouldAddProductToFavoritesFromProductPage() {
        MainPage mainPage = new MainPage();
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        ProductPage productPage = searchResultsPage.openFirstSearchResultProduct();
        String productName = productPage.getProductName();
        assertThat(productPage.isProductInFavorites())
                .as("До добавления сердце на странице товара серое").isFalse();

        productPage.addToFavorites();
        assertThat(productPage.isProductInFavorites())
                .as("Сердце на странице товара красное").isTrue();

        FavoritesPage favoritesPage = mainPage.openFavorites();
        assertThat(favoritesPage.isFavoriteProductDisplayed(productName))
                .as("Товар появился в избранном").isTrue();
    }

    @Test
    @DisplayName("3. Добавление в избранное из результатов поиска")
    public void shouldAddProductToFavoritesFromSearchResults() {
        MainPage mainPage = new MainPage();
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        String productName = searchResultsPage.getFirstSearchResultProductName();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as("До добавления сердце в результатах поиска серое").isFalse();

        searchResultsPage.addFirstSearchResultToFavorites();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as("Сердце стало активным").isTrue();

        FavoritesPage favoritesPage = mainPage.openFavorites();
        assertThat(favoritesPage.isFavoriteProductDisplayed(productName))
                .as("Выбранный товар находится в избранном").isTrue();
    }

    @Test
    @DisplayName("4. Удаление товара из избранного")
    public void shouldRemoveProductFromFavorites() {
        String preparedProductName = accountStateService
                .addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        int headerFavoritesCountBeforeRemoval = mainPage.getHeaderFavoritesCount();
        int titleFavoritesCountBeforeRemoval = favoritesPage.getFavoritesTitleProductCount();

        favoritesPage.removeProductFromFavorites(preparedProductName);

        assertThat(favoritesPage.isProductInFavorites(preparedProductName))
                .as("Сердце стало серым").isFalse();
        assertThat(mainPage.getHeaderFavoritesCount()).as("Счётчик в шапке уменьшился")
                .isEqualTo(headerFavoritesCountBeforeRemoval - FAVORITES_COUNT_DECREMENT);
        assertThat(favoritesPage.getFavoritesTitleProductCount())
                .as("Счётчик возле заголовка уменьшился")
                .isEqualTo(titleFavoritesCountBeforeRemoval - FAVORITES_COUNT_DECREMENT);

        favoritesPage.refreshPage();
        assertThat(favoritesPage.isFavoriteProductDisplayed(preparedProductName))
                .as("После обновления товара нет").isFalse();
    }

    @Test
    @DisplayName("6. Обратный порядок добавления товаров")
    public void shouldDisplayNewestFavoritesFirst() {
        MainPage mainPage = new MainPage();
        String firstAddedProductName = addFirstSearchResultToFavorites(
                mainPage, PRIMARY_ELECTRONICS);
        String secondAddedProductName = addFirstSearchResultToFavorites(
                mainPage, SECOND_ELECTRONICS);
        String thirdAddedProductName = addFirstSearchResultToFavorites(
                mainPage, THIRD_ELECTRONICS);

        List<String> favoriteProductNames = mainPage.openFavorites().getFavoriteProductNames();
        assertThat(favoriteProductNames).contains(
                firstAddedProductName, secondAddedProductName, thirdAddedProductName);
        assertThat(favoriteProductNames.indexOf(thirdAddedProductName))
                .as("Третий добавленный отображается выше второго")
                .isLessThan(favoriteProductNames.indexOf(secondAddedProductName));
        assertThat(favoriteProductNames.indexOf(secondAddedProductName))
                .as("Второй добавленный отображается выше первого")
                .isLessThan(favoriteProductNames.indexOf(firstAddedProductName));
    }

    @Test
    @DisplayName("9. Синхронизация сердца между поиском и страницей товара")
    public void shouldSynchronizeFavoriteStateBetweenSearchAndProductPage() {
        MainPage mainPage = new MainPage();
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as("Изначально сердце серое").isFalse();

        searchResultsPage.toggleFirstSearchResultFavoriteState();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as("В поиске сердце стало красным").isTrue();

        ProductPage productPage = searchResultsPage.openFirstSearchResultProduct();
        assertThat(productPage.isProductInFavorites())
                .as("На странице товара сердце красное").isTrue();
    }

    private String addFirstSearchResultToFavorites(MainPage mainPage, String searchQuery) {
        SearchResultsPage searchResultsPage = mainPage.search(searchQuery);
        String productName = searchResultsPage.getFirstSearchResultProductName();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as("До добавления товар отсутствует в избранном: " + productName).isFalse();
        searchResultsPage.addFirstSearchResultToFavorites();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as("Добавлен товар: " + productName).isTrue();
        return productName;
    }
}
