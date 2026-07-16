package elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import core.BaseElement;
import core.XPathLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.match;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

/** Список карточек избранных товаров и доступные действия с ними. */
public class FavoriteProductGrid extends BaseElement {

    private static final String INNER_HTML_PROPERTY = "innerHTML";
    private static final String CONTENT_CHANGED_DESCRIPTION = "состав списка товаров изменился";
    private static final String PRODUCT_CARD_RELATIVE_XPATH =
            "//div[contains(concat(' ', normalize-space(@class), ' '), ' tile-root ')]";
    private static final String PRODUCT_NAME_DESCENDANT_XPATH =
            "//a[contains(@href, '/product/')][normalize-space()]";
    private static final String PRODUCT_LINK_WITH_NAME_RELATIVE_XPATH_TEMPLATE =
            ".//a[contains(@href, '/product/') and contains(normalize-space(.), %s)]";
    private static final String HEART_DESCENDANT_XPATH =
            "//button[.//*[name()='svg'][@width='24' and @height='24']"
                    + "/*[name()='path' and (@fill='white'"
                    + " or translate(@fill, 'abcdef', 'ABCDEF')='#F8104B')]]";
    private static final String HEART_RELATIVE_XPATH = "." + HEART_DESCENDANT_XPATH;
    private static final String CART_BUTTON_RELATIVE_XPATH =
            "//button[normalize-space()"
                    + " and .//*[name()='svg'][@viewBox='0 0 16 16']]";
    private static final String PRICE_RELATIVE_XPATH =
            "//span[contains(normalize-space(.), '₽')]";
    private static final String CART_STATE_RELATIVE_XPATH =
            "//*[self::input[@inputmode='decimal']"
                    + " or (self::div and count(./button)=2"
                    + " and ./span[normalize-space()"
                    + " and number(normalize-space())=number(normalize-space())])]";

    private final String rootXPath;

    private FavoriteProductGrid(String rootXPath) {
        super($x(rootXPath));
        this.rootXPath = rootXPath;
    }

    public int getProductCount() {
        waitUntilVisible();
        return getProductCards().size();
    }

    public List<String> getProductNames() {
        waitUntilVisible();
        List<String> productNames = new ArrayList<>();
        for (SelenideElement productCard : getProductCards().asFixedIterable()) {
            String productName = TextElement.from(
                    productCard.$x(getRelativeProductNameXPath())).getText();
            if (!productName.isBlank()) {
                productNames.add(productName);
            }
        }
        return productNames;
    }

    public String getFirstProductName() {
        return TextElement.byXPath(getProductNameXPath(
                getFirstProductCardXPath())).getText();
    }

    public String getFirstProductNameAvailableForCart() {
        return TextElement.byXPath(getProductNameXPath(
                getFirstProductCardAvailableForCartXPath())).getText();
    }

    public boolean isProductDisplayed(String productName) {
        return VisibleElement.byXPath(getProductCardByNameXPath(productName)).isDisplayed();
    }

    public boolean isFirstProductNameAndPriceDisplayed() {
        String firstProductCardXPath = getFirstProductCardXPath();
        return TextElement.byXPath(getProductNameXPath(
                firstProductCardXPath)).isDisplayed()
                && TextElement.byXPath(firstProductCardXPath
                + PRICE_RELATIVE_XPATH).isDisplayed();
    }

    public boolean isAnyCartButtonDisplayed() {
        return Button.byXPath("(" + getProductCardsXPath()
                + CART_BUTTON_RELATIVE_XPATH + ")[1]").isDisplayed();
    }

    public void removeProductFromFavorites(String productName) {
        HeartIcon.byXPath(getProductCardByNameXPath(productName)
                + HEART_DESCENDANT_XPATH).toggleFavoriteState();
    }

    public boolean isProductInFavorites(String productName) {
        if (!isProductDisplayed(productName)) {
            return false;
        }
        return HeartIcon.byXPath(getProductCardByNameXPath(productName)
                + HEART_DESCENDANT_XPATH).isActive();
    }

    public void addProductToCart(String productName) {
        Button.byXPath("(" + getProductCardByNameXPath(productName)
                + CART_BUTTON_RELATIVE_XPATH + ")[1]").click();
    }

    public boolean isProductInCart(String productName) {
        return VisibleElement.byXPath(getProductCardByNameXPath(productName)
                + CART_STATE_RELATIVE_XPATH).isDisplayed();
    }

    public boolean isAnyProductPresent() {
        return !getProductCards().isEmpty();
    }

    /** Выключает активные сердца у текущего загруженного набора карточек. */
    public void removeVisibleProducts() {
        for (SelenideElement productCard : getProductCards().asFixedIterable()) {
            HeartIcon favoriteHeart = HeartIcon.from(productCard.$x(HEART_RELATIVE_XPATH));
            if (favoriteHeart.isActive()) {
                favoriteHeart.toggleFavoriteState();
            }
        }
    }

    public String getContentState() {
        return waitUntilVisible().getDomProperty(INNER_HTML_PROPERTY);
    }

    public void waitUntilContentChanges(String previousContentState) {
        element.shouldHave(match(
                CONTENT_CHANGED_DESCRIPTION,
                currentElement -> !Objects.equals(previousContentState,
                        currentElement.getDomProperty(INNER_HTML_PROPERTY))),
                ELEMENT_WAIT_TIMEOUT);
    }

    private ElementsCollection getProductCards() {
        return $$x(getProductCardsXPath());
    }

    private String getProductCardsXPath() {
        return rootXPath + PRODUCT_CARD_RELATIVE_XPATH;
    }

    private String getFirstProductCardXPath() {
        return "(" + getProductCardsXPath() + ")[1]";
    }

    private String getFirstProductCardAvailableForCartXPath() {
        return "(" + getProductCardsXPath()
                + "[." + CART_BUTTON_RELATIVE_XPATH + "])[1]";
    }

    private String getProductCardByNameXPath(String productName) {
        return getProductCardsXPath() + "["
                + String.format(PRODUCT_LINK_WITH_NAME_RELATIVE_XPATH_TEMPLATE,
                XPathLiteral.from(productName)) + "]";
    }

    private String getProductNameXPath(String productCardXPath) {
        return "(" + productCardXPath + PRODUCT_NAME_DESCENDANT_XPATH + ")[last()]";
    }

    private String getRelativeProductNameXPath() {
        return "(." + PRODUCT_NAME_DESCENDANT_XPATH + ")[last()]";
    }

    public static FavoriteProductGrid byXPath(String rootXPath) {
        return new FavoriteProductGrid(rootXPath);
    }
}
