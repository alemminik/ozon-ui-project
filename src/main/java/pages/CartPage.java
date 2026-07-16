package pages;

import core.BasePage;
import elements.CartControls;
import elements.VisibleElement;
import elements.VisibleElementCollection;

/** Раздел «Корзина». */
public class CartPage extends BasePage {

    private static final String CART_PAGE_XPATH = "//div[@data-widget='cartSplit']";
    private static final String CART_ITEMS_XPATH = CART_PAGE_XPATH
            + "/*[.//input[@type='checkbox'] and .//span[normalize-space()]]";
    private static final String CART_CONTROLS_XPATH = "//div[@data-widget='controls']";
    private static final String CART_PAGE_STATE_XPATH = CART_CONTROLS_XPATH
            + " | //div[@data-widget='emptyCart']";

    private final CartControls cartControls = CartControls.byXPath(CART_CONTROLS_XPATH);
    private final VisibleElementCollection cartItems =
            VisibleElementCollection.byXPath(CART_ITEMS_XPATH);
    private final VisibleElement cartPageState = VisibleElement.byXPath(CART_PAGE_STATE_XPATH);

    public boolean isProductDisplayed(String productName) {
        return cartItems.isElementWithTextDisplayed(productName);
    }

    public void removeAllProductsFromCart() {
        if (cartControls.isPresent()) {
            cartControls.removeAllProducts();
        }
    }

    CartPage waitUntilLoaded() {
        cartPageState.waitUntilDisplayed();
        return this;
    }

    boolean isLoaded() {
        return cartPageState.isPresent();
    }
}
