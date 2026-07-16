package core;

import org.openqa.selenium.support.ui.FluentWait;
import pages.FavoritesPage;
import pages.MainPage;
import pages.SearchResultsPage;

import java.time.Duration;

/** Подготавливает и очищает данные отдельного тестового аккаунта через UI. */
public class AccountStateService {

    private static final Duration CATEGORY_FILTER_READY_TIMEOUT = Duration.ofSeconds(45);
    private static final Duration CATEGORY_FILTER_POLL_INTERVAL = Duration.ofSeconds(2);

    /** Полностью очищает избранное и корзину независимо от текущей страницы. */
    public void clearFavoritesAndCart() {
        try {
            FavoritesPage favoritesPage = new MainPage().openFavorites();
            favoritesPage.removeAllProductsFromFavorites();
        } finally {
            new MainPage().openCart().removeAllProductsFromCart();
        }
    }

    /** Добавляет первый результат поиска и возвращает его фактическое название. */
    public String addFirstSearchResultToFavorites(String searchQuery) {
        SearchResultsPage searchResultsPage = new MainPage().search(searchQuery);
        String productName = searchResultsPage.getFirstSearchResultProductName();
        if (searchResultsPage.isFirstSearchResultInFavorites()) {
            throw new IllegalStateException(
                    "Товар уже находится в избранном после очистки: " + productName);
        }
        searchResultsPage.addFirstSearchResultToFavorites();
        return productName;
    }

    /** Обновляет избранное до появления рассчитанного сервером фильтра категорий. */
    public void waitUntilFavoritesCategoryFilterReady() {
        FavoritesPage favoritesPage = new MainPage().openFavorites();
        new FluentWait<>(favoritesPage)
                .withTimeout(CATEGORY_FILTER_READY_TIMEOUT)
                .pollingEvery(CATEGORY_FILTER_POLL_INTERVAL)
                .withMessage("Ozon не подготовил фильтр категорий избранного")
                .until(currentPage -> {
                    if (currentPage.isCategoryFilterPresent()) {
                        return true;
                    }
                    currentPage.refreshPage();
                    return false;
                });
    }
}
