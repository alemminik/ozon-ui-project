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
    private static final String PRODUCT_PAGE_TEST_DISPLAY_NAME =
            "2. Добавление в избранное со страницы товара";
    private static final String SEARCH_RESULTS_TEST_DISPLAY_NAME =
            "3. Добавление в избранное из результатов поиска";
    private static final String REMOVAL_TEST_DISPLAY_NAME = "4. Удаление товара из избранного";
    private static final String ORDER_TEST_DISPLAY_NAME = "6. Обратный порядок добавления товаров";
    private static final String SYNCHRONIZATION_TEST_DISPLAY_NAME =
            "9. Синхронизация сердца между поиском и страницей товара";
    private static final String PRODUCT_PAGE_INITIAL_STATE_ASSERTION =
            "До добавления сердце на странице товара серое";
    private static final String PRODUCT_PAGE_ACTIVE_STATE_ASSERTION =
            "Сердце на странице товара красное";
    private static final String PRODUCT_ADDED_ASSERTION = "Товар появился в избранном";
    private static final String SEARCH_INITIAL_STATE_ASSERTION =
            "До добавления сердце в результатах поиска серое";
    private static final String SEARCH_ACTIVE_STATE_ASSERTION = "Сердце стало активным";
    private static final String SELECTED_PRODUCT_ASSERTION =
            "Выбранный товар находится в избранном";
    private static final String HEART_INACTIVE_ASSERTION = "Сердце стало серым";
    private static final String HEADER_COUNTER_ASSERTION = "Счётчик в шапке уменьшился";
    private static final String TITLE_COUNTER_ASSERTION = "Счётчик возле заголовка уменьшился";
    private static final String PRODUCT_REMOVED_ASSERTION = "После обновления товара нет";
    private static final String THIRD_PRODUCT_ORDER_ASSERTION =
            "Третий добавленный отображается выше второго";
    private static final String SECOND_PRODUCT_ORDER_ASSERTION =
            "Второй добавленный отображается выше первого";
    private static final String INITIAL_HEART_STATE_ASSERTION = "Изначально сердце серое";
    private static final String SEARCH_HEART_SYNCHRONIZED_ASSERTION =
            "В поиске сердце стало красным";
    private static final String PRODUCT_HEART_SYNCHRONIZED_ASSERTION =
            "На странице товара сердце красное";
    private static final String PRODUCT_ABSENT_ASSERTION_TEMPLATE =
            "До добавления товар отсутствует в избранном: %s";
    private static final String PRODUCT_ADDED_ASSERTION_TEMPLATE = "Добавлен товар: %s";

    @Test
    @DisplayName(PRODUCT_PAGE_TEST_DISPLAY_NAME)
    public void shouldAddProductToFavoritesFromProductPage() {
        MainPage mainPage = new MainPage();
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        ProductPage productPage = searchResultsPage.openFirstSearchResultProduct();
        String productName = productPage.getProductName();
        assertThat(productPage.isProductInFavorites())
                .as(PRODUCT_PAGE_INITIAL_STATE_ASSERTION).isFalse();

        productPage.addToFavorites();
        assertThat(productPage.isProductInFavorites())
                .as(PRODUCT_PAGE_ACTIVE_STATE_ASSERTION).isTrue();

        FavoritesPage favoritesPage = mainPage.openFavorites();
        assertThat(favoritesPage.isFavoriteProductDisplayed(productName))
                .as(PRODUCT_ADDED_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(SEARCH_RESULTS_TEST_DISPLAY_NAME)
    public void shouldAddProductToFavoritesFromSearchResults() {
        MainPage mainPage = new MainPage();
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        String productName = searchResultsPage.getFirstSearchResultProductName();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(SEARCH_INITIAL_STATE_ASSERTION).isFalse();

        searchResultsPage.addFirstSearchResultToFavorites();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(SEARCH_ACTIVE_STATE_ASSERTION).isTrue();

        FavoritesPage favoritesPage = mainPage.openFavorites();
        assertThat(favoritesPage.isFavoriteProductDisplayed(productName))
                .as(SELECTED_PRODUCT_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(REMOVAL_TEST_DISPLAY_NAME)
    public void shouldRemoveProductFromFavorites() {
        String preparedProductName = accountStateService
                .addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        int headerFavoritesCountBeforeRemoval = mainPage.getHeaderFavoritesCount();
        int titleFavoritesCountBeforeRemoval = favoritesPage.getFavoritesTitleProductCount();

        favoritesPage.removeProductFromFavorites(preparedProductName);

        assertThat(favoritesPage.isProductInFavorites(preparedProductName))
                .as(HEART_INACTIVE_ASSERTION).isFalse();
        assertThat(mainPage.getHeaderFavoritesCount()).as(HEADER_COUNTER_ASSERTION)
                .isEqualTo(headerFavoritesCountBeforeRemoval - FAVORITES_COUNT_DECREMENT);
        assertThat(favoritesPage.getFavoritesTitleProductCount())
                .as(TITLE_COUNTER_ASSERTION)
                .isEqualTo(titleFavoritesCountBeforeRemoval - FAVORITES_COUNT_DECREMENT);

        favoritesPage.refreshPage();
        assertThat(favoritesPage.isFavoriteProductDisplayed(preparedProductName))
                .as(PRODUCT_REMOVED_ASSERTION).isFalse();
    }

    @Test
    @DisplayName(ORDER_TEST_DISPLAY_NAME)
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
                .as(THIRD_PRODUCT_ORDER_ASSERTION)
                .isLessThan(favoriteProductNames.indexOf(secondAddedProductName));
        assertThat(favoriteProductNames.indexOf(secondAddedProductName))
                .as(SECOND_PRODUCT_ORDER_ASSERTION)
                .isLessThan(favoriteProductNames.indexOf(firstAddedProductName));
    }

    @Test
    @DisplayName(SYNCHRONIZATION_TEST_DISPLAY_NAME)
    public void shouldSynchronizeFavoriteStateBetweenSearchAndProductPage() {
        MainPage mainPage = new MainPage();
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(INITIAL_HEART_STATE_ASSERTION).isFalse();

        searchResultsPage.toggleFirstSearchResultFavoriteState();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(SEARCH_HEART_SYNCHRONIZED_ASSERTION).isTrue();

        ProductPage productPage = searchResultsPage.openFirstSearchResultProduct();
        assertThat(productPage.isProductInFavorites())
                .as(PRODUCT_HEART_SYNCHRONIZED_ASSERTION).isTrue();
    }

    private String addFirstSearchResultToFavorites(MainPage mainPage, String searchQuery) {
        SearchResultsPage searchResultsPage = mainPage.search(searchQuery);
        String productName = searchResultsPage.getFirstSearchResultProductName();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(String.format(PRODUCT_ABSENT_ASSERTION_TEMPLATE, productName)).isFalse();
        searchResultsPage.addFirstSearchResultToFavorites();
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(String.format(PRODUCT_ADDED_ASSERTION_TEMPLATE, productName)).isTrue();
        return productName;
    }
}
