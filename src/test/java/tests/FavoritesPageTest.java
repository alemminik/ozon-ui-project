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

    @Test
    @DisplayName(COMPONENTS_TEST_DISPLAY_NAME)
    public void shouldDisplayFavoritesSectionComponents() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(SECOND_NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(HOME_APPLIANCES);
        accountStateService.addFirstSearchResultToFavorites(PET_SUPPLIES);
        accountStateService.addFirstSearchResultToFavorites(BOOKS);
        accountStateService.waitUntilFavoritesCategoryFilterReady();
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();

        assertThat(favoritesPage.isFavoritesTitleDisplayed()).as(FAVORITES_TITLE_ASSERTION).isTrue();
        assertThat(favoritesPage.isFavoritesListDisplayed()).as(FAVORITES_LIST_ASSERTION).isTrue();
        assertThat(favoritesPage.isCategoryFilterDisplayed()).as(CATEGORY_FILTER_ASSERTION).isTrue();
        assertThat(favoritesPage.isFirstFavoriteProductNameAndPriceDisplayed())
                .as(PRODUCT_NAME_AND_PRICE_ASSERTION).isTrue();
        assertThat(favoritesPage.isAnyFavoriteProductCartButtonDisplayed())
                .as(ADD_TO_CART_BUTTON_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(REFRESH_TEST_DISPLAY_NAME)
    public void shouldPreserveFavoritesAfterPageRefresh() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        String firstProductName = favoritesPage.getFirstFavoriteProductName();
        int favoritesCountBeforeRefresh = favoritesPage.getFavoriteProductCount();

        favoritesPage.refreshPage();

        assertThat(favoritesPage.getFavoriteProductCount()).as(PRODUCT_COUNT_PRESERVED_ASSERTION)
                .isEqualTo(favoritesCountBeforeRefresh);
        assertThat(favoritesPage.isFavoriteProductDisplayed(firstProductName))
                .as(PRODUCT_PRESERVED_ASSERTION).isTrue();
    }

    @Test
    @DisplayName(FILTER_TEST_DISPLAY_NAME)
    public void shouldFilterFavoritesByCategory() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(SECOND_NON_ELECTRONICS);
        accountStateService.addFirstSearchResultToFavorites(HOME_APPLIANCES);
        accountStateService.addFirstSearchResultToFavorites(PET_SUPPLIES);
        accountStateService.addFirstSearchResultToFavorites(BOOKS);
        accountStateService.waitUntilFavoritesCategoryFilterReady();
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        int totalFavoritesCount = favoritesPage.getFavoriteProductCount();

        favoritesPage.selectFavoritesCategory(ELECTRONICS_CATEGORY_NAME);

        assertThat(favoritesPage.getFavoriteProductCount()).as(FILTERED_PRODUCT_COUNT_ASSERTION)
                .isLessThan(totalFavoritesCount);

        favoritesPage.clearFavoritesCategoryFilter();
        assertThat(favoritesPage.getFavoriteProductCount()).as(RESET_PRODUCT_COUNT_ASSERTION)
                .isEqualTo(totalFavoritesCount);
    }

    @Test
    @DisplayName(RECOMMENDATIONS_TEST_DISPLAY_NAME)
    public void shouldLoadRecommendationsAfterScroll() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        assertThat(favoritesPage.isFavoritesListDisplayed()).as(FAVORITES_LIST_SHORT_ASSERTION).isTrue();

        favoritesPage.scrollToRecommendations();
        assertThat(favoritesPage.isRecommendationsDisplayed())
                .as(RECOMMENDATIONS_SECTION_ASSERTION).isTrue();
        int recommendationsCountBeforeLoading = favoritesPage.getRecommendationsCount();

        favoritesPage.loadMoreRecommendations();

        assertThat(favoritesPage.getRecommendationsCount()).as(MORE_RECOMMENDATIONS_ASSERTION)
                .isGreaterThan(recommendationsCountBeforeLoading);
    }
}
