package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.FavoritesPage;
import pages.MainPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SvincovFavoritesTest extends BaseTest {

    @Test
    @DisplayName("1. Корректное отображение раздела «Избранное»")
    public void favoritesSectionDisplayed() {
        MainPage main = new MainPage().openMain();
        FavoritesPage favorites = main.openFavorites();
        assertThat(favorites.isTitleDisplayed()).as("Заголовок «Избранное»").isTrue();
        assertThat(favorites.isListDisplayed()).as("Список добавленных товаров").isTrue();
        assertThat(favorites.isCategoryFilterDisplayed()).as("Фильтр категорий").isTrue();
        assertThat(favorites.isFirstCardHasNameAndPrice()).as("Название и цена в карточке").isTrue();
        assertThat(favorites.isCartButtonDisplayedInCard()).as("Кнопка добавления в корзину").isTrue();
    }

    @Test
    @DisplayName("5. Сохранение списка избранного после обновления")
    public void favoritesPersistAfterRefresh() {
        MainPage main = new MainPage().openMain();
        FavoritesPage favorites = main.openFavorites();
        String productName = favorites.getFirstProductName();
        int before = favorites.getItemsCount();

        favorites.refreshPage();

        assertThat(favorites.getItemsCount()).as("Количество товаров сохранилось").isEqualTo(before);
        assertThat(favorites.hasProduct(productName)).as("Товар остался после обновления").isTrue();
    }

    @Test
    @DisplayName("10. Подгрузка рекомендаций под разделом «Избранное»")
    public void recommendationsLoadOnScroll() {
        MainPage main = new MainPage().openMain();
        FavoritesPage favorites = main.openFavorites();
        assertThat(favorites.isListDisplayed()).as("Список избранного").isTrue();

        favorites.scrollDown();
        assertThat(favorites.isRecommendationsDisplayed()).as("Блок рекомендаций").isTrue();
        int before = favorites.getRecommendationsCount();

        favorites.scrollDown();

        assertThat(favorites.getRecommendationsCount()).as("Подгрузились новые рекомендации")
                .isGreaterThan(before);
    }
}
