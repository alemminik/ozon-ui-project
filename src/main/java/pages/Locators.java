package pages;

/**
 * Централизованное хранилище XPath-локаторов Ozon.
 * Ozon часто меняет вёрстку и имена классов, поэтому все селекторы собраны в одном месте —
 * при поломке тестов достаточно поправить локатор здесь.
 *
 * Значения ниже сверены с сохранённым DOM главной страницы, выдачи, карточки товара,
 * избранного и корзины. В приоритете семантические data-widget и структура элементов.
 */
public final class Locators {

    private Locators() {
    }

    // --- Шапка сайта (главная и все страницы) ---
    public static final String SEARCH_INPUT = "//div[@data-widget='searchBarDesktop']//input[@name='text']";
    public static final String HEADER_FAVORITES_LINK = "//a[@data-widget='favoriteCounter']";
    public static final String HEADER_CART_LINK = "//a[@data-widget='headerIcon' and contains(@href, '/cart')]";
    // Счётчик (бейдж) с числом товаров рядом с иконкой сердца в шапке
    public static final String HEADER_FAVORITES_COUNTER =
            "//a[@data-widget='favoriteCounter']/div[normalize-space() and not(.//span)]";

    // --- Результаты поиска ---
    // Карточка товара в выдаче по видимому названию (%s)
    public static final String SEARCH_CARD_BY_NAME =
            "//div[@data-widget='tileGridDesktop']"
                    + "//div[contains(concat(' ', normalize-space(@class), ' '), ' tile-root ')"
                    + " and .//a[contains(@href, '/product/') and contains(normalize-space(.), '%s')]]";
    // Иконка сердца внутри карточки товара в выдаче (%s — название)
    public static final String SEARCH_CARD_HEART_BY_NAME =
            "(" + SEARCH_CARD_BY_NAME + "//button[not(normalize-space())])[last()]";
    // Первый товар в выдаче
    public static final String SEARCH_FIRST_CARD =
            "(//div[@data-widget='tileGridDesktop']"
                    + "//div[contains(concat(' ', normalize-space(@class), ' '), ' tile-root ')])[1]";
    public static final String SEARCH_FIRST_CARD_HEART =
            "(" + SEARCH_FIRST_CARD + "//button[not(normalize-space())])[last()]";
    // Ссылка на товар по названию (для открытия карточки)
    public static final String SEARCH_PRODUCT_LINK_BY_NAME =
            SEARCH_CARD_BY_NAME + "//a[contains(@href, '/product/') and normalize-space()][1]";
    public static final String SEARCH_FIRST_PRODUCT_LINK =
            SEARCH_FIRST_CARD + "//a[contains(@href, '/product/')][normalize-space()][1]";
    public static final String SEARCH_FIRST_PRODUCT_NAME =
            "(" + SEARCH_FIRST_CARD + "//a[contains(@href, '/product/')][normalize-space()])[last()]";

    // --- Страница товара ---
    public static final String PRODUCT_TITLE = "//div[@data-widget='webProductHeading']//h1";
    public static final String PRODUCT_HEART = "(//div[@data-widget='webAddToFavorite']//button)[1]";
    public static final String PRODUCT_ADD_TO_CART =
            "(//div[@data-widget='webAddToCart']//button[.//span[normalize-space()='В корзину']])[1]";

    // --- Раздел «Избранное» ---
    public static final String FAVORITES_TITLE =
            "//div[@data-widget='tabs']//div[normalize-space()='Избранное']";
    public static final String FAVORITES_LIST =
            "(//div[@data-widget='tabs']/following::div[@data-widget='paginator'"
                    + " and .//div[@data-widget='tileGridDesktop']])[1]";
    public static final String FAVORITES_ITEMS = FAVORITES_LIST
            + "//div[contains(concat(' ', normalize-space(@class), ' '), ' tile-root ')]";
    // Счётчик количества рядом с надписью «Избранное»
    public static final String FAVORITES_HEADER_COUNTER =
            FAVORITES_TITLE + "/following-sibling::div[1]";
    // Карточка избранного по названию (%s)
    public static final String FAVORITES_CARD_BY_NAME = FAVORITES_ITEMS
            + "[.//a[contains(@href, '/product/') and contains(normalize-space(.), '%s')]]";
    public static final String FAVORITES_CARD_HEART_BY_NAME =
            "(" + FAVORITES_CARD_BY_NAME + "//button[not(normalize-space())])[1]";
    public static final String FAVORITES_CARD_CART_BY_NAME =
            "(" + FAVORITES_CARD_BY_NAME + "//button[normalize-space()])[1]";
    // Блок фильтрации по категориям
    public static final String FAVORITES_CATEGORY_FILTER = "//div[@data-widget='filtersDesktop']";
    public static final String FAVORITES_CATEGORY_OPTION =
            FAVORITES_CATEGORY_FILTER + "//a[normalize-space()='%s']";
    public static final String FAVORITES_CATEGORY_ALL = FAVORITES_TITLE;
    // Блок рекомендованных товаров под избранным
    public static final String RECOMMENDATIONS_BLOCK = "//div[@data-widget='skuGrid']";
    public static final String RECOMMENDATIONS_ITEMS = RECOMMENDATIONS_BLOCK
            + "//div[contains(concat(' ', normalize-space(@class), ' '), ' tile-root ')]";

    // --- Корзина ---
    public static final String CART_ITEMS =
            "//div[@data-widget='cartSplit']/*[.//input[@type='checkbox'] and .//span[normalize-space()]]";
    public static final String CART_ITEM_BY_NAME = CART_ITEMS
            + "[.//span[contains(normalize-space(.), '%s')]]";
}
