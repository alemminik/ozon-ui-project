package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.FavoritesPage;
import pages.MainPage;

import static core.TestProductQueries.BOOKS;
import static core.TestProductQueries.HOME_APPLIANCES;
import static core.TestProductQueries.NON_ELECTRONICS;
import static core.TestProductQueries.PET_SUPPLIES;
import static core.TestProductQueries.PRIMARY_ELECTRONICS;
import static core.TestProductQueries.SECOND_NON_ELECTRONICS;
import static org.assertj.core.api.Assertions.assertThat;

/** Проверки содержимого, фильтрации и рекомендаций раздела «Избранное». */
public class FavoritesPageTest extends BaseTest {

    private static final String ELECTRONICS_CATEGORY_NAME = "Электроника";
    private static final String COMPONENTS_TEST_DISPLAY_NAME =
            "1. Корректное отображение раздела «Избранное»";
    private static final String REFRESH_TEST_DISPLAY_NAME =
            "5. Сохранение списка избранного после обновления";
    private static final String FILTER_TEST_DISPLAY_NAME =
            "7. Фильтрация избранного по категории";
    private static final String RECOMMENDATIONS_TEST_DISPLAY_NAME =
            "10. Подгрузка рекомендаций под разделом «Избранное»";
    private static final String FAVORITES_TITLE_ASSERTION = "Заголовок «Избранное»";
    private static final String FAVORITES_LIST_ASSERTION = "Список добавленных товаров";
    private static final String CATEGORY_FILTER_ASSERTION = "Фильтр категорий";
    private static final String PRODUCT_NAME_AND_PRICE_ASSERTION = "Название и цена в карточке";
    private static final String ADD_TO_CART_BUTTON_ASSERTION = "Кнопка добавления в корзину";
    private static final String PRODUCT_COUNT_PRESERVED_ASSERTION = "Количество товаров сохранилось";
    private static final String PRODUCT_PRESERVED_ASSERTION = "Товар остался после обновления";
    private static final String FILTERED_PRODUCT_COUNT_ASSERTION = "После фильтра товаров меньше";
    private static final String RESET_PRODUCT_COUNT_ASSERTION =
            "После сброса снова показаны все товары";
    private static final String FAVORITES_LIST_SHORT_ASSERTION = "Список избранного";
    private static final String RECOMMENDATIONS_SECTION_ASSERTION = "Блок рекомендаций";
    private static final String MORE_RECOMMENDATIONS_ASSERTION =
            "Подгрузились новые рекомендации";
    private static final String COMPONENTS_STEP_1 =
            "По каждому запросу выполнить поиск и добавить в избранное первый товар из результатов.";
    private static final String COMPONENTS_STEP_2 =
            "Открыть раздел «Избранное» и, при необходимости, обновлять страницу до появления фильтра категорий.";
    private static final String COMPONENTS_STEP_3 =
            "Проверить отображение заголовка «Избранное».";
    private static final String COMPONENTS_STEP_4 =
            "Проверить отображение списка добавленных товаров.";
    private static final String COMPONENTS_STEP_5 =
            "Проверить отображение блока фильтрации по категориям.";
    private static final String COMPONENTS_STEP_6 =
            "Проверить, что в первой карточке отображаются название и цена товара.";
    private static final String COMPONENTS_STEP_7 =
            "Проверить наличие хотя бы одной кнопки добавления товара в корзину.";
    private static final String REFRESH_STEP_1 = "Открыть раздел «Избранное».";
    private static final String REFRESH_STEP_2 =
            "Сохранить название первого избранного товара.";
    private static final String REFRESH_STEP_3 =
            "Подсчитать количество карточек в списке избранного.";
    private static final String REFRESH_STEP_4 = "Обновить страницу.";
    private static final String REFRESH_STEP_5 = "Повторно подсчитать количество карточек.";
    private static final String REFRESH_STEP_6 =
            "Проверить равенство количества карточек до и после обновления.";
    private static final String REFRESH_STEP_7 =
            "Проверить наличие товара с сохранённым названием";
    private static final String FILTER_STEP_1 =
            "Подготовить избранное по всем шести поисковым запросам.";
    private static final String FILTER_STEP_2 =
            "Ожидать появления фильтра категорий, при необходимости обновляя страницу.";
    private static final String FILTER_STEP_3 =
            "Открыть раздел «Избранное» и подсчитать общее количество карточек.";
    private static final String FILTER_STEP_4 =
            "Выбрать категорию «Электроника» и дождаться изменения состава списка.";
    private static final String FILTER_STEP_5 =
            "Проверить, что карточек стало меньше, чем до фильтрации.";
    private static final String FILTER_STEP_6 =
            "Выбрать «Все категории» и дождаться изменения состава списка.";
    private static final String FILTER_STEP_7 =
            "Проверить, что количество карточек вернулось к исходному.";
    private static final String RECOMMENDATIONS_STEP_1 = "Открыть раздел «Избранное».";
    private static final String RECOMMENDATIONS_STEP_2 =
            "Проверить отображение списка избранных товаров.";
    private static final String RECOMMENDATIONS_STEP_3 =
            "Прокрутить страницу к блоку рекомендаций «Подобрано для вас».";
    private static final String RECOMMENDATIONS_STEP_4 =
            "Проверить отображение блока рекомендаций.";
    private static final String RECOMMENDATIONS_STEP_5 =
            "Подсчитать количество карточек рекомендаций.";
    private static final String RECOMMENDATIONS_STEP_6 =
            "Прокрутить страницу к нижней границе последней карточки.";
    private static final String RECOMMENDATIONS_STEP_7 =
            "Дождаться увеличения количества карточек и проверить, что оно стало больше исходного.";

    @Test
    @DisplayName(COMPONENTS_TEST_DISPLAY_NAME)
    public void shouldDisplayFavoritesSectionComponents() {
        logStep(COMPONENTS_STEP_1);
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(SECOND_NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(HOME_APPLIANCES);
        accountStateService.addFirstSearchResultToFavorites(PET_SUPPLIES);
        accountStateService.addFirstSearchResultToFavorites(BOOKS);
        logStep(COMPONENTS_STEP_2);
        accountStateService.waitUntilFavoritesCategoryFilterReady();
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();

        logStep(COMPONENTS_STEP_3);
        assertThat(favoritesPage.isFavoritesTitleDisplayed()).as(FAVORITES_TITLE_ASSERTION).isTrue();
        logStep(COMPONENTS_STEP_4);
        assertThat(favoritesPage.isFavoritesListDisplayed()).as(FAVORITES_LIST_ASSERTION).isTrue();
        logStep(COMPONENTS_STEP_5);
        assertThat(favoritesPage.isCategoryFilterDisplayed()).as(CATEGORY_FILTER_ASSERTION).isTrue();
        logStep(COMPONENTS_STEP_6);
        assertThat(favoritesPage.isFirstFavoriteProductNameAndPriceDisplayed())
                .as(PRODUCT_NAME_AND_PRICE_ASSERTION).isTrue();
        logStep(COMPONENTS_STEP_7);
        assertThat(favoritesPage.isAnyFavoriteProductCartButtonDisplayed())
                .as(ADD_TO_CART_BUTTON_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(REFRESH_TEST_DISPLAY_NAME)
    public void shouldPreserveFavoritesAfterPageRefresh() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        logStep(REFRESH_STEP_1);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        logStep(REFRESH_STEP_2);
        String firstProductName = favoritesPage.getFirstFavoriteProductName();
        logStep(REFRESH_STEP_3);
        int favoritesCountBeforeRefresh = favoritesPage.getFavoriteProductCount();

        logStep(REFRESH_STEP_4);
        favoritesPage.refreshPage();

        logStep(REFRESH_STEP_5);
        int favoritesCountAfterRefresh = favoritesPage.getFavoriteProductCount();
        logStep(REFRESH_STEP_6);
        assertThat(favoritesCountAfterRefresh).as(PRODUCT_COUNT_PRESERVED_ASSERTION)
                .isEqualTo(favoritesCountBeforeRefresh);
        logStep(REFRESH_STEP_7);
        assertThat(favoritesPage.isFavoriteProductDisplayed(firstProductName))
                .as(PRODUCT_PRESERVED_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(FILTER_TEST_DISPLAY_NAME)
    public void shouldFilterFavoritesByCategory() {
        logStep(FILTER_STEP_1);
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(SECOND_NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(HOME_APPLIANCES);
        accountStateService.addFirstSearchResultToFavorites(PET_SUPPLIES);
        accountStateService.addFirstSearchResultToFavorites(BOOKS);
        logStep(FILTER_STEP_2);
        accountStateService.waitUntilFavoritesCategoryFilterReady();
        MainPage mainPage = new MainPage();
        logStep(FILTER_STEP_3);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        int totalFavoritesCount = favoritesPage.getFavoriteProductCount();

        logStep(FILTER_STEP_4);
        favoritesPage.selectFavoritesCategory(ELECTRONICS_CATEGORY_NAME);

        logStep(FILTER_STEP_5);
        assertThat(favoritesPage.getFavoriteProductCount()).as(FILTERED_PRODUCT_COUNT_ASSERTION)
                .isLessThan(totalFavoritesCount);

        logStep(FILTER_STEP_6);
        favoritesPage.clearFavoritesCategoryFilter();
        logStep(FILTER_STEP_7);
        assertThat(favoritesPage.getFavoriteProductCount()).as(RESET_PRODUCT_COUNT_ASSERTION)
                .isEqualTo(totalFavoritesCount);
    }

    @Test
    @DisplayName(RECOMMENDATIONS_TEST_DISPLAY_NAME)
    public void shouldLoadRecommendationsAfterScroll() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        logStep(RECOMMENDATIONS_STEP_1);
        FavoritesPage favoritesPage = mainPage.openFavorites();
        logStep(RECOMMENDATIONS_STEP_2);
        assertThat(favoritesPage.isFavoritesListDisplayed()).as(FAVORITES_LIST_SHORT_ASSERTION).isTrue();

        logStep(RECOMMENDATIONS_STEP_3);
        favoritesPage.scrollToRecommendations();
        logStep(RECOMMENDATIONS_STEP_4);
        assertThat(favoritesPage.isRecommendationsDisplayed())
                .as(RECOMMENDATIONS_SECTION_ASSERTION).isTrue();
        logStep(RECOMMENDATIONS_STEP_5);
        int recommendationsCountBeforeLoading = favoritesPage.getRecommendationsCount();

        logStep(RECOMMENDATIONS_STEP_6);
        favoritesPage.loadMoreRecommendations();

        logStep(RECOMMENDATIONS_STEP_7);
        assertThat(favoritesPage.getRecommendationsCount()).as(MORE_RECOMMENDATIONS_ASSERTION)
                .isGreaterThan(recommendationsCountBeforeLoading);
    }
}
