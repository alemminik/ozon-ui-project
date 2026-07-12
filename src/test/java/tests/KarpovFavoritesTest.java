package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.FavoritesPage;
import pages.MainPage;
import pages.SearchResultsPage;

import static org.assertj.core.api.Assertions.assertThat;

public class KarpovFavoritesTest extends BaseTest {

    @Test
    @DisplayName("3. Добавление в избранное из результатов поиска")
    public void addToFavoritesFromSearchResults() {
        MainPage main = new MainPage().openMain();
        SearchResultsPage results = main.search("Наушники JBL Tune 520BT");
        String actualProductName = results.getFirstProductName();

        results.addFirstToFavorites();
        assertThat(results.isFirstHeartActive()).as("Сердце стало активным").isTrue();

        FavoritesPage favorites = main.openFavorites();
        assertThat(favorites.hasProduct(actualProductName))
                .as("Выбранный товар находится в избранном").isTrue();
    }

    @Test
    @DisplayName("7. Фильтрация избранного по категории")
    public void filterFavoritesByCategory() {
        MainPage main = new MainPage().openMain();
        FavoritesPage favorites = main.openFavorites();
        int total = favorites.getItemsCount();
        favorites.selectCategory("Электроника");

        assertThat(favorites.getItemsCount()).as("После фильтра товаров меньше")
                .isLessThan(total);

        favorites.selectAllCategories();
        assertThat(favorites.getItemsCount()).as("После сброса снова показаны все товары")
                .isEqualTo(total);
    }
}
