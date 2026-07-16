package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Selenide.$x;

/** Общие действия над выбранными товарами корзины. */
public class CartControls extends BaseElement {

    private static final String SELECT_ALL_CHECKBOX_RELATIVE_XPATH =
            ".//input[@type='checkbox']";
    private static final String DELETE_SELECTED_BUTTON_RELATIVE_XPATH =
            ".//button[not(.//*[normalize-space()='Поделиться'])]";
    private static final String CONFIRM_DELETION_BUTTON_XPATH =
            "//button[normalize-space()='Удалить товары'"
                    + " or normalize-space()='Удалить']";

    private CartControls(SelenideElement element) {
        super(element);
    }

    /** Выбирает и удаляет все товары, если корзина не пуста. */
    public void removeAllProducts() {
        waitUntilVisible();
        SelenideElement deleteSelectedButton =
                element.$x(DELETE_SELECTED_BUTTON_RELATIVE_XPATH);
        if (!deleteSelectedButton.exists()) {
            return;
        }

        Checkbox.from(element.$x(SELECT_ALL_CHECKBOX_RELATIVE_XPATH)).select();
        Button.from(deleteSelectedButton).click();
        Button.byXPath(CONFIRM_DELETION_BUTTON_XPATH).click();
        deleteSelectedButton.should(disappear, ELEMENT_WAIT_TIMEOUT);
    }

    public static CartControls byXPath(String xpathExpression) {
        return new CartControls($x(xpathExpression));
    }
}
