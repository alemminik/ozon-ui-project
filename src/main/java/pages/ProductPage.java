package pages;

import core.BasePage;
import elements.Button;
import elements.HeartIcon;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Страница товара.
 * Позволяет добавить товар в избранное, узнать состояние иконки сердца,
 * добавить товар в корзину и получить название товара.
 */
public class ProductPage extends BasePage {

    private final HeartIcon heart = HeartIcon.byXpath(Locators.PRODUCT_HEART);
    private final Button addToCartButton = Button.byXpath(Locators.PRODUCT_ADD_TO_CART);

    /** Нажать иконку сердца (добавить/убрать из избранного). */
    public void clickHeart() {
        heart.toggle();
    }

    public void addToFavorites() {
        if (!heart.isActive()) {
            heart.toggle();
        }
        heart.waitForState(true);
    }

    public void ensureHeartActive(boolean active) {
        if (heart.isActive() != active) {
            heart.toggle();
            heart.waitForState(active);
        }
    }

    /** Активна ли иконка сердца (товар в избранном). */
    public boolean isHeartActive() {
        return heart.isActive();
    }

    /** Название товара. */
    public String getTitle() {
        return $x(Locators.PRODUCT_TITLE).getText();
    }

    /** Добавить товар в корзину. */
    public void addToCart() {
        addToCartButton.press();
    }

    public void ensureInCart() {
        if ($x(Locators.PRODUCT_ADD_TO_CART).exists()) {
            addToCart();
        }
    }
}
