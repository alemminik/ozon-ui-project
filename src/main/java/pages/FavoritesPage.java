package pages;

import com.codeborne.selenide.ElementsCollection;
import core.BasePage;
import elements.Button;
import elements.HeartIcon;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Раздел «Избранное».
 * Позволяет работать со списком избранных товаров, фильтром по категориям,
 * блоком рекомендаций и счётчиками. Проверки (assert) выполняются в тестах —
 * страница только возвращает данные и выполняет действия.
 */
public class FavoritesPage extends BasePage {

    /** Отображается ли заголовок «Избранное». */
    public boolean isTitleDisplayed() {
        return exists(Locators.FAVORITES_TITLE);
    }

    /** Отображается ли список товаров. */
    public boolean isListDisplayed() {
        return exists(Locators.FAVORITES_LIST);
    }

    /** Отображается ли блок фильтрации по категориям. */
    public boolean isCategoryFilterDisplayed() {
        return exists(Locators.FAVORITES_CATEGORY_FILTER);
    }

    /** Количество товаров в списке избранного. */
    public int getItemsCount() {
        if (!items().isEmpty()) {
            items().first().shouldBe(visible, Duration.ofSeconds(15));
        }
        return items().size();
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        for (var item : items().asFixedIterable()) {
            String name = item.$x("(.//a[contains(@href, '/product/')][normalize-space()])[last()]")
                    .getText().trim();
            if (!name.isBlank()) {
                names.add(name);
            }
        }
        return names;
    }

    public String getFirstProductName() {
        return items().first()
                .shouldBe(visible, Duration.ofSeconds(15))
                .$x("(.//a[contains(@href, '/product/')][normalize-space()])[last()]")
                .getText().trim();
    }

    public String getFirstAvailableForCartProductName() {
        var card = $x("(" + Locators.FAVORITES_ITEMS + "[.//button[normalize-space()]])[1]")
                .shouldBe(visible, Duration.ofSeconds(15));
        return card.$x("(.//a[contains(@href, '/product/')][normalize-space()])[last()]")
                .getText().trim();
    }

    /** Есть ли в списке товар с указанным названием. */
    public boolean hasProduct(String name) {
        return exists(String.format(Locators.FAVORITES_CARD_BY_NAME, name));
    }

    /** Сколько карточек с указанным названием в списке (для проверки отсутствия дублей). */
    public int countProduct(String name) {
        return $$x(String.format(Locators.FAVORITES_CARD_BY_NAME, name)).size();
    }

    /** Отображаются ли у карточки товара его название и цена (проверка первой карточки). */
    public boolean isFirstCardHasNameAndPrice() {
        if (getItemsCount() == 0) {
            return false;
        }
        var first = items().first();
        boolean hasName = first.$x("(.//a[contains(@href, '/product/')][normalize-space()])[last()]")
                .is(visible);
        boolean hasPrice = first.$x(".//*[contains(normalize-space(.), '₽')]").is(visible);
        return hasName && hasPrice;
    }

    /** Отображается ли в карточке товара кнопка добавления в корзину. */
    public boolean isCartButtonDisplayedInCard() {
        ElementsCollection carts = $$x(Locators.FAVORITES_ITEMS + "//button[normalize-space()]");
        return !carts.isEmpty() && carts.first().is(com.codeborne.selenide.Condition.visible);
    }

    /** Нажать иконку сердца у товара с указанным названием (удалить из избранного). */
    public void removeByName(String name) {
        HeartIcon.byXpath(String.format(Locators.FAVORITES_CARD_HEART_BY_NAME, name)).toggle();
    }

    /** Активна ли иконка сердца у товара с указанным названием. */
    public boolean isHeartActiveByName(String name) {
        if (!$x(String.format(Locators.FAVORITES_CARD_BY_NAME, name)).exists()) {
            return false;
        }
        return HeartIcon.byXpath(String.format(Locators.FAVORITES_CARD_HEART_BY_NAME, name)).isActive();
    }

    /** Значение счётчика количества рядом с заголовком «Избранное». */
    public int getHeaderCounter() {
        try {
            String text = $x(Locators.FAVORITES_HEADER_COUNTER).getText().replaceAll("\\D+", "");
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Throwable e) {
            return 0;
        }
    }

    /** Выбрать категорию в блоке фильтрации. */
    public void selectCategory(String category) {
        String href = $x(String.format(Locators.FAVORITES_CATEGORY_OPTION, category))
                .shouldBe(visible, Duration.ofSeconds(15)).getAttribute("href");
        open(href);
    }

    public String getFirstAvailableCategory() {
        return $x(Locators.FAVORITES_CATEGORY_FILTER + "//a[normalize-space()][1]")
                .shouldBe(visible, Duration.ofSeconds(15)).getText().trim();
    }

    /** Сбросить фильтр — выбрать категорию «Все». */
    public void selectAllCategories() {
        open("https://www.ozon.ru/my/favorites");
        $x(Locators.FAVORITES_TITLE).shouldBe(visible, Duration.ofSeconds(15));
    }

    /** Нажать кнопку корзины у товара с указанным названием (добавить в корзину). */
    public void addToCartByName(String name) {
        Button.byXpath(String.format(Locators.FAVORITES_CARD_CART_BY_NAME, name)).press();
    }

    /** Изменилась ли кнопка в карточке товара на состояние «В корзине». */
    public boolean isInCartStateByName(String name) {
        String card = String.format(Locators.FAVORITES_CARD_BY_NAME, name);
        String addButton = String.format(Locators.FAVORITES_CARD_CART_BY_NAME, name);
        String controls = card + "//*[self::button[contains(., 'В корзине')]"
                + " or @type='addToCartButtonWithQuantity'"
                + " or self::input[@inputmode='decimal']]";
        long deadline = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();
        while (System.currentTimeMillis() < deadline) {
            if ($x(controls).exists() || ($x(card).exists() && !$x(addButton).exists())) {
                return true;
            }
            sleep(250);
        }
        return false;
    }

    // --- Прокрутка и рекомендации ---

    /** Прокрутить страницу вниз. */
    public void scrollDown() {
        com.codeborne.selenide.Selenide.executeJavaScript("window.scrollBy(0, document.body.scrollHeight);");
        com.codeborne.selenide.Selenide.sleep(1500);
    }

    /** Отображается ли блок рекомендованных товаров. */
    public boolean isRecommendationsDisplayed() {
        return exists(Locators.RECOMMENDATIONS_BLOCK);
    }

    /** Количество карточек в блоке рекомендаций. */
    public int getRecommendationsCount() {
        return $$x(Locators.RECOMMENDATIONS_ITEMS).size();
    }

    private boolean exists(String xpath) {
        try {
            return $x(xpath).shouldBe(visible, Duration.ofSeconds(15)).isDisplayed();
        } catch (Throwable e) {
            return false;
        }
    }

    private ElementsCollection items() {
        return $$x(Locators.FAVORITES_ITEMS);
    }
}
