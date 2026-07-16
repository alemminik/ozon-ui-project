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

    private final Checkbox selectAllCheckbox;
    private final Button deleteSelectedButton;
    private final ButtonCollection confirmationButtons;

    private CartControls(SelenideElement element) {
        super(element);
        selectAllCheckbox = Checkbox.from(element.$x(SELECT_ALL_CHECKBOX_RELATIVE_XPATH));
        deleteSelectedButton = Button.from(
                element.$x(DELETE_SELECTED_BUTTON_RELATIVE_XPATH));
        confirmationButtons = ButtonCollection.byXPath(CONFIRM_DELETION_BUTTON_XPATH);
    }

    /** Выбирает и удаляет все товары, если корзина не пуста. */
    public void removeAllProducts() {
        waitUntilVisible();
        if (!deleteSelectedButton.isPresent()) {
            return;
        }

        selectAllCheckbox.select();
        deleteSelectedButton.click();
        confirmationButtons.clickFirstVisible();
        element.should(disappear, ELEMENT_WAIT_TIMEOUT);
    }

    public static CartControls byXPath(String xpathExpression) {
        return new CartControls($x(xpathExpression));
    }
}
