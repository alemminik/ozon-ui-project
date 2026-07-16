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
    private static final String PRODUCT_PAGE_STEP_1 =
            "Выполнить поиск по заданному запросу.";
    private static final String PRODUCT_PAGE_STEP_2 =
            "Открыть страницу первого товара из результатов поиска.";
    private static final String PRODUCT_PAGE_STEP_3 =
            "Сохранить фактическое название открытого товара.";
    private static final String PRODUCT_PAGE_STEP_4 =
            "Проверить, что до добавления иконка сердца неактивна.";
    private static final String PRODUCT_PAGE_STEP_5 = "Нажать на иконку сердца.";
    private static final String PRODUCT_PAGE_STEP_6 =
            "Проверить, что иконка сердца стала активной.";
    private static final String PRODUCT_PAGE_STEP_7 = "Перейти в раздел «Избранное».";
    private static final String PRODUCT_PAGE_STEP_8 =
            "Проверить наличие товара с сохранённым фактическим названием.";
    private static final String SEARCH_RESULTS_STEP_1 =
            "Выполнить поиск по заданному запросу.";
    private static final String SEARCH_RESULTS_STEP_2 =
            "Сохранить фактическое название первого товара в результатах поиска.";
    private static final String SEARCH_RESULTS_STEP_3 =
            "Проверить, что до добавления иконка сердца в первой карточке неактивна.";
    private static final String SEARCH_RESULTS_STEP_4 =
            "Нажать на иконку сердца в первой карточке.";
    private static final String SEARCH_RESULTS_STEP_5 =
            "Проверить, что иконка сердца стала активной.";
    private static final String SEARCH_RESULTS_STEP_6 = "Перейти в раздел «Избранное».";
    private static final String SEARCH_RESULTS_STEP_7 =
            "Проверить наличие товара с сохранённым фактическим названием.";
    private static final String REMOVAL_STEP_1 = "Открыть раздел «Избранное».";
    private static final String REMOVAL_STEP_2 =
            "Считать количество избранных товаров в счётчике шапки и в счётчике рядом с заголовком.";
    private static final String REMOVAL_STEP_3 =
            "Нажать на активную иконку сердца в карточке подготовленного товара.";
    private static final String REMOVAL_STEP_4 =
            "Проверить, что товар больше не имеет активного состояния избранного.";
    private static final String REMOVAL_STEP_5 =
            "Проверить, что счётчик в шапке уменьшился на 1.";
    private static final String REMOVAL_STEP_6 =
            "Проверить, что счётчик рядом с заголовком уменьшился на 1.";
    private static final String REMOVAL_STEP_7 = "Обновить страницу.";
    private static final String REMOVAL_STEP_8 =
            "Проверить отсутствие товара с сохранённым названием.";
    private static final String ORDER_STEP_1 =
            "Выполнить поиск по первому запросу, проверить неактивное сердце, добавить первый результат в избранное и проверить активное состояние.";
    private static final String ORDER_STEP_2 =
            "Повторить добавление для второго запроса.";
    private static final String ORDER_STEP_3 =
            "Повторить добавление для третьего запроса.";
    private static final String ORDER_STEP_4 =
            "Открыть раздел «Избранное» и получить названия всех отображаемых товаров.";
    private static final String ORDER_STEP_5 =
            "Проверить наличие всех трёх сохранённых названий.";
    private static final String ORDER_STEP_6 =
            "Проверить, что третий добавленный товар расположен выше второго.";
    private static final String ORDER_STEP_7 =
            "Проверить, что второй добавленный товар расположен выше первого.";
    private static final String SYNCHRONIZATION_STEP_1 =
            "Выполнить поиск по заданному запросу.";
    private static final String SYNCHRONIZATION_STEP_2 =
            "Проверить, что иконка сердца в первой карточке неактивна.";
    private static final String SYNCHRONIZATION_STEP_3 =
            "Нажать на иконку сердца в первой карточке.";
    private static final String SYNCHRONIZATION_STEP_4 =
            "Проверить, что в результатах поиска иконка стала активной.";
    private static final String SYNCHRONIZATION_STEP_5 =
            "Открыть страницу первого товара.";
    private static final String SYNCHRONIZATION_STEP_6 =
            "Проверить, что на странице товара иконка сердца также активна.";

    @Test
    @DisplayName(PRODUCT_PAGE_TEST_DISPLAY_NAME)
    public void shouldAddProductToFavoritesFromProductPage() {
        MainPage mainPage = new MainPage();
        logStep(PRODUCT_PAGE_STEP_1);
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        logStep(PRODUCT_PAGE_STEP_2);
        ProductPage productPage = searchResultsPage.openFirstSearchResultProduct();
        logStep(PRODUCT_PAGE_STEP_3);
        String productName = productPage.getProductName();
        logStep(PRODUCT_PAGE_STEP_4);
        assertThat(productPage.isProductInFavorites())
                .as(PRODUCT_PAGE_INITIAL_STATE_ASSERTION).isFalse();

        logStep(PRODUCT_PAGE_STEP_5);
        productPage.addToFavorites();
        logStep(PRODUCT_PAGE_STEP_6);
        assertThat(productPage.isProductInFavorites())
                .as(PRODUCT_PAGE_ACTIVE_STATE_ASSERTION).isTrue();

        logStep(PRODUCT_PAGE_STEP_7);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        logStep(PRODUCT_PAGE_STEP_8);
        assertThat(favoritesPage.isFavoriteProductDisplayed(productName))
                .as(PRODUCT_ADDED_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(SEARCH_RESULTS_TEST_DISPLAY_NAME)
    public void shouldAddProductToFavoritesFromSearchResults() {
        MainPage mainPage = new MainPage();
        logStep(SEARCH_RESULTS_STEP_1);
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        logStep(SEARCH_RESULTS_STEP_2);
        String productName = searchResultsPage.getFirstSearchResultProductName();
        logStep(SEARCH_RESULTS_STEP_3);
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(SEARCH_INITIAL_STATE_ASSERTION).isFalse();

        logStep(SEARCH_RESULTS_STEP_4);
        searchResultsPage.addFirstSearchResultToFavorites();
        logStep(SEARCH_RESULTS_STEP_5);
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(SEARCH_ACTIVE_STATE_ASSERTION).isTrue();

        logStep(SEARCH_RESULTS_STEP_6);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        logStep(SEARCH_RESULTS_STEP_7);
        assertThat(favoritesPage.isFavoriteProductDisplayed(productName))
                .as(SELECTED_PRODUCT_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(REMOVAL_TEST_DISPLAY_NAME)
    public void shouldRemoveProductFromFavorites() {
        String preparedProductName = accountStateService
                .addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        logStep(REMOVAL_STEP_1);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        logStep(REMOVAL_STEP_2);
        int headerFavoritesCountBeforeRemoval = mainPage.getHeaderFavoritesCount();
        int titleFavoritesCountBeforeRemoval = favoritesPage.getFavoritesTitleProductCount();

        logStep(REMOVAL_STEP_3);
        favoritesPage.removeProductFromFavorites(preparedProductName);

        logStep(REMOVAL_STEP_4);
        assertThat(favoritesPage.isProductInFavorites(preparedProductName))
                .as(HEART_INACTIVE_ASSERTION).isFalse();
        logStep(REMOVAL_STEP_5);
        assertThat(mainPage.getHeaderFavoritesCount()).as(HEADER_COUNTER_ASSERTION)
                .isEqualTo(headerFavoritesCountBeforeRemoval - FAVORITES_COUNT_DECREMENT);
        logStep(REMOVAL_STEP_6);
        assertThat(favoritesPage.getFavoritesTitleProductCount())
                .as(TITLE_COUNTER_ASSERTION)
                .isEqualTo(titleFavoritesCountBeforeRemoval - FAVORITES_COUNT_DECREMENT);

        logStep(REMOVAL_STEP_7);
        favoritesPage.refreshPage();
        logStep(REMOVAL_STEP_8);
        assertThat(favoritesPage.isFavoriteProductAbsent(preparedProductName))
                .as(PRODUCT_REMOVED_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(ORDER_TEST_DISPLAY_NAME)
    public void shouldDisplayNewestFavoritesFirst() {
        MainPage mainPage = new MainPage();
        logStep(ORDER_STEP_1);
        String firstAddedProductName = addFirstSearchResultToFavorites(
                mainPage, PRIMARY_ELECTRONICS);
        logStep(ORDER_STEP_2);
        String secondAddedProductName = addFirstSearchResultToFavorites(
                mainPage, SECOND_ELECTRONICS);
        logStep(ORDER_STEP_3);
        String thirdAddedProductName = addFirstSearchResultToFavorites(
                mainPage, THIRD_ELECTRONICS);

        logStep(ORDER_STEP_4);
        List<String> favoriteProductNames = mainPage.openFavorites().getFavoriteProductNames();
        logStep(ORDER_STEP_5);
        assertThat(favoriteProductNames).contains(
                firstAddedProductName, secondAddedProductName, thirdAddedProductName);
        logStep(ORDER_STEP_6);
        assertThat(favoriteProductNames.indexOf(thirdAddedProductName))
                .as(THIRD_PRODUCT_ORDER_ASSERTION)
                .isLessThan(favoriteProductNames.indexOf(secondAddedProductName));
        logStep(ORDER_STEP_7);
        assertThat(favoriteProductNames.indexOf(secondAddedProductName))
                .as(SECOND_PRODUCT_ORDER_ASSERTION)
                .isLessThan(favoriteProductNames.indexOf(firstAddedProductName));
    }

    @Test
    @DisplayName(SYNCHRONIZATION_TEST_DISPLAY_NAME)
    public void shouldSynchronizeFavoriteStateBetweenSearchAndProductPage() {
        MainPage mainPage = new MainPage();
        logStep(SYNCHRONIZATION_STEP_1);
        SearchResultsPage searchResultsPage = mainPage.search(PRIMARY_ELECTRONICS);
        logStep(SYNCHRONIZATION_STEP_2);
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(INITIAL_HEART_STATE_ASSERTION).isFalse();

        logStep(SYNCHRONIZATION_STEP_3);
        searchResultsPage.toggleFirstSearchResultFavoriteState();
        logStep(SYNCHRONIZATION_STEP_4);
        assertThat(searchResultsPage.isFirstSearchResultInFavorites())
                .as(SEARCH_HEART_SYNCHRONIZED_ASSERTION).isTrue();

        logStep(SYNCHRONIZATION_STEP_5);
        ProductPage productPage = searchResultsPage.openFirstSearchResultProduct();
        logStep(SYNCHRONIZATION_STEP_6);
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
