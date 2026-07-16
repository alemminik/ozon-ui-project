package pages;

import core.BasePage;
import elements.Counter;
import elements.FavoriteProductGrid;
import elements.Link;
import elements.LinkCollection;
import elements.RecommendationGrid;
import elements.VisibleElement;

import java.util.List;

/** Раздел «Избранное»: товары, категории, корзина и рекомендации. */
public class FavoritesPage extends BasePage {

    private static final int MAX_FAVORITES_CLEANUP_PASSES = 20;
    private static final String FAVORITES_TITLE_XPATH =
            "//div[@data-widget='tabs']//div[normalize-space()='Избранное']";
    private static final String FAVORITES_LIST_XPATH =
            "(//div[@data-widget='tabs']/following::div"
                    + "[@data-widget='tileGridDesktop'])[1]";
    private static final String FAVORITES_HEADER_COUNTER_XPATH =
            FAVORITES_TITLE_XPATH + "/following-sibling::div[1]";
    private static final String CATEGORY_FILTER_XPATH =
            "//div[@data-widget='filtersDesktop']";
    private static final String CATEGORY_LINKS_XPATH =
            CATEGORY_FILTER_XPATH + "//a[normalize-space()]";
    private static final String ALL_CATEGORIES_LINK_XPATH =
            CATEGORY_FILTER_XPATH + "//a[normalize-space()='Все категории']";
    private static final String RECOMMENDATIONS_SECTION_XPATH =
            "//div[@data-widget='paginator'"
                    + " and .//span[normalize-space()='Подобрано для вас']]";
    private static final String FAVORITES_CLEANUP_FAILURE_MESSAGE =
            "Не удалось полностью очистить избранное через UI";

    private final VisibleElement favoritesTitle = VisibleElement.byXPath(FAVORITES_TITLE_XPATH);
    private final VisibleElement favoritesList = VisibleElement.byXPath(FAVORITES_LIST_XPATH);
    private final VisibleElement categoryFilter = VisibleElement.byXPath(CATEGORY_FILTER_XPATH);
    private final Counter favoritesHeaderCounter = Counter.byXPath(FAVORITES_HEADER_COUNTER_XPATH);
    private final LinkCollection categoryLinks = LinkCollection.byXPath(CATEGORY_LINKS_XPATH);
    private final Link allCategoriesLink = Link.byXPath(ALL_CATEGORIES_LINK_XPATH);
    private final FavoriteProductGrid favoriteProducts =
            FavoriteProductGrid.byXPath(FAVORITES_LIST_XPATH);
    private final RecommendationGrid recommendations =
            RecommendationGrid.byXPath(RECOMMENDATIONS_SECTION_XPATH);

    public boolean isFavoritesTitleDisplayed() {
        return favoritesTitle.isDisplayed();
    }

    public boolean isFavoritesListDisplayed() {
        return favoritesList.isDisplayed();
    }

    public boolean isCategoryFilterDisplayed() {
        return categoryFilter.isDisplayed();
    }

    public boolean isCategoryFilterPresent() {
        return categoryFilter.isPresent();
    }

    public int getFavoriteProductCount() {
        return favoriteProducts.getProductCount();
    }

    public List<String> getFavoriteProductNames() {
        return favoriteProducts.getProductNames();
    }

    public String getFirstFavoriteProductName() {
        return favoriteProducts.getFirstProductName();
    }

    public String getFirstAvailableFavoriteProductNameForCart() {
        return favoriteProducts.getFirstProductNameAvailableForCart();
    }

    public boolean isFavoriteProductDisplayed(String productName) {
        return favoriteProducts.isProductDisplayed(productName);
    }

    public boolean isFavoriteProductAbsent(String productName) {
        return favoriteProducts.isProductAbsent(productName);
    }

    public boolean isFirstFavoriteProductNameAndPriceDisplayed() {
        return favoriteProducts.isFirstProductNameAndPriceDisplayed();
    }

    public boolean isAnyFavoriteProductCartButtonDisplayed() {
        return favoriteProducts.isAnyCartButtonDisplayed();
    }

    public void removeProductFromFavorites(String productName) {
        favoriteProducts.removeProductFromFavorites(productName);
    }

    public boolean isProductInFavorites(String productName) {
        return favoriteProducts.isProductInFavorites(productName);
    }

    public int getFavoritesTitleProductCount() {
        return favoritesHeaderCounter.getValue();
    }

    /** Выбирает категорию и ожидает завершения обновления списка. */
    public FavoritesPage selectFavoritesCategory(String categoryName) {
        String contentStateBeforeFiltering = favoriteProducts.getContentState();
        categoryLinks.clickByExactText(categoryName);
        favoriteProducts.waitUntilContentChanges(contentStateBeforeFiltering);
        return this;
    }

    /** Возвращает полный список через пользовательский контрол сброса фильтра. */
    public FavoritesPage clearFavoritesCategoryFilter() {
        String contentStateBeforeReset = favoriteProducts.getContentState();
        allCategoriesLink.click();
        favoriteProducts.waitUntilContentChanges(contentStateBeforeReset);
        return this;
    }

    public void addFavoriteProductToCart(String productName) {
        favoriteProducts.addProductToCart(productName);
    }

    public boolean isProductInCart(String productName) {
        return favoriteProducts.isProductInCart(productName);
    }

    public void removeAllProductsFromFavorites() {
        for (int cleanupPass = 0;
             cleanupPass < MAX_FAVORITES_CLEANUP_PASSES
                     && favoriteProducts.isAnyProductPresent();
             cleanupPass++) {
            favoriteProducts.removeFirstVisibleProduct();
            refreshPage();
        }
        if (favoriteProducts.isAnyProductPresent()) {
            throw new IllegalStateException(FAVORITES_CLEANUP_FAILURE_MESSAGE);
        }
    }

    public void scrollToRecommendations() {
        recommendations.scrollIntoView();
    }

    public boolean isRecommendationsDisplayed() {
        return recommendations.isDisplayed();
    }

    public int getRecommendationsCount() {
        return recommendations.getProductCount();
    }

    /** Прокручивает рекомендации и ожидает появления новых карточек. */
    public void loadMoreRecommendations() {
        recommendations.loadMoreProducts();
    }

    @Override
    public FavoritesPage refreshPage() {
        super.refreshPage();
        return waitUntilLoaded();
    }

    FavoritesPage waitUntilLoaded() {
        waitUntilDocumentReady();
        favoritesTitle.waitUntilDisplayed();
        return this;
    }

    boolean isLoaded() {
        return favoritesTitle.isPresent();
    }
}
