package pages;

import core.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

/** Раздел «Корзина». */
public class CartPage extends BasePage {

    public boolean hasProduct(String name) {
        try {
            return $x(String.format(Locators.CART_ITEM_BY_NAME, name)).is(visible);
        } catch (Throwable e) {
            return false;
        }
    }
}
