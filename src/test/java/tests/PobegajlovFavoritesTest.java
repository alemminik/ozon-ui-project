package tests;

import core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.FavoritesPage;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultsPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PobegajlovFavoritesTest extends BaseTest {

    private static final String PRODUCT = "Наушники JBL Tune 520BT";

    @Test
    @DisplayName("2. Добавление в избранное со страницы товара")
    public void addToFavoritesFromProductPage() {
        MainPage main = new MainPage().openMain();
        SearchResultsPage results = main.search(PRODUCT);
        ProductPage product = results.openProductByName(PRODUCT);
        String actualProductName = product.getTitle();

        product.addToFavorites();
        assertThat(product.isHeartActive()).as("Сердце на странице товара красное").isTrue();

        FavoritesPage favorites = main.openFavorites();
        assertThat(favorites.hasProduct(actualProductName)).as("Товар появился в избранном").isTrue();
    }

    @Test
    @DisplayName("6. Обратный порядок добавления товаров")
    public void favoritesOrder() {
        MainPage main = new MainPage().openMain();
        String first = addFirstResult(main, "Наушники JBL Tune 520BT");
        String second = addFirstResult(main, "Мышь Logitech G102");
        String third = addFirstResult(main, "Клавиатура Redragon Kumara");

        List<String> names = main.openFavorites().getItemNames();
        assertThat(names).contains(first, second, third);
        assertThat(names.indexOf(third)).as("Третий добавленный отображается выше второго")
                .isLessThan(names.indexOf(second));
        assertThat(names.indexOf(second)).as("Второй добавленный отображается выше первого")
                .isLessThan(names.indexOf(first));
    }

    @Test
    @DisplayName("9. Синхронизация сердца между поиском и страницей товара")
    public void heartStateSync() {
        MainPage main = new MainPage().openMain();
        SearchResultsPage results = main.search(PRODUCT);
        assertThat(results.isFirstHeartActive()).as("Изначально сердце серое").isFalse();

        results.clickFirstHeart();
        assertThat(results.isFirstHeartActive()).as("В поиске сердце стало красным").isTrue();

        ProductPage product = results.openFirstProduct();
        assertThat(product.isHeartActive()).as("На странице товара сердце красное").isTrue();
    }

    private String addFirstResult(MainPage main, String query) {
        SearchResultsPage results = main.search(query);
        String name = results.getFirstProductName();
        results.clickFirstHeart();
        assertThat(results.isFirstHeartActive()).as("Добавлен товар: " + name).isTrue();
        return name;
    }
}
