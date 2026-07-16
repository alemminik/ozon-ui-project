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

    @Test
    @DisplayName("1. Корректное отображение раздела «Избранное»")
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

        assertThat(favoritesPage.isFavoritesTitleDisplayed()).as("Заголовок «Избранное»").isTrue();
        assertThat(favoritesPage.isFavoritesListDisplayed()).as("Список добавленных товаров").isTrue();
        assertThat(favoritesPage.isCategoryFilterDisplayed()).as("Фильтр категорий").isTrue();
        assertThat(favoritesPage.isFirstFavoriteProductNameAndPriceDisplayed())
                .as("Название и цена в карточке").isTrue();
        assertThat(favoritesPage.isAnyFavoriteProductCartButtonDisplayed())
                .as("Кнопка добавления в корзину").isTrue();
    }

    @Test
    @DisplayName("5. Сохранение списка избранного после обновления")
    public void shouldPreserveFavoritesAfterPageRefresh() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        String firstProductName = favoritesPage.getFirstFavoriteProductName();
        int favoritesCountBeforeRefresh = favoritesPage.getFavoriteProductCount();

        favoritesPage.refreshPage();

        assertThat(favoritesPage.getFavoriteProductCount()).as("Количество товаров сохранилось")
                .isEqualTo(favoritesCountBeforeRefresh);
        assertThat(favoritesPage.isFavoriteProductDisplayed(firstProductName))
                .as("Товар остался после обновления").isTrue();
    }

    @Test
    @DisplayName("7. Фильтрация избранного по категории")
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

        assertThat(favoritesPage.getFavoriteProductCount()).as("После фильтра товаров меньше")
                .isLessThan(totalFavoritesCount);

        favoritesPage.clearFavoritesCategoryFilter();
        assertThat(favoritesPage.getFavoriteProductCount()).as("После сброса снова показаны все товары")
                .isEqualTo(totalFavoritesCount);
    }

    @Test
    @DisplayName("10. Подгрузка рекомендаций под разделом «Избранное»")
    public void shouldLoadRecommendationsAfterScroll() {
        accountStateService.addFirstSearchResultToFavorites(PRIMARY_ELECTRONICS);
        MainPage mainPage = new MainPage();
        FavoritesPage favoritesPage = mainPage.openFavorites();
        assertThat(favoritesPage.isFavoritesListDisplayed()).as("Список избранного").isTrue();

        favoritesPage.scrollToRecommendations();
        assertThat(favoritesPage.isRecommendationsDisplayed()).as("Блок рекомендаций").isTrue();
        int recommendationsCountBeforeLoading = favoritesPage.getRecommendationsCount();

        favoritesPage.loadMoreRecommendations();

        assertThat(favoritesPage.getRecommendationsCount()).as("Подгрузились новые рекомендации")
                .isGreaterThan(recommendationsCountBeforeLoading);
    }
}
