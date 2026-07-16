package elements;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.UIAssertionError;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;

import static com.codeborne.selenide.Condition.match;
import static com.codeborne.selenide.Selenide.$x;

/** Иконка управления состоянием товара в избранном. */
public class HeartIcon extends ClickableElement {

    private static final String ARIA_PRESSED_ATTRIBUTE = "aria-pressed";
    private static final String ARIA_CHECKED_ATTRIBUTE = "aria-checked";
    private static final String ARIA_LABEL_ATTRIBUTE = "aria-label";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String TRUE_ATTRIBUTE_VALUE = "true";
    private static final String ADD_TO_FAVORITES_LABEL = "добавить в избранное";
    private static final String REMOVE_FROM_FAVORITES_LABEL = "удалить из избранного";
    private static final String CLEAR_FROM_FAVORITES_LABEL = "убрать из избранного";
    private static final String ACTIVE_CLASS_MARKER = "active";
    private static final String SELECTED_CLASS_MARKER = "selected";
    private static final String CSS_CLASS_SEPARATOR_PATTERN = "\\s+";
    private static final String ACTIVE_HEART_FILL_COLOR = "#F8104B";
    private static final String ACTIVE_SVG_PATH_XPATH =
            ".//*[name()='path' and translate(@fill, 'abcdef', 'ABCDEF')='"
                    + ACTIVE_HEART_FILL_COLOR + "']";
    private static final String EXPECTED_STATE_DESCRIPTION = "состояние избранного: %s";
    private static final Duration FIRST_STATE_CHANGE_TIMEOUT = Duration.ofSeconds(5);

    private HeartIcon(SelenideElement element) {
        super(element);
    }

    /** Кликает по иконке и ожидает изменения состояния. */
    @Override
    public void click() {
        boolean wasActive = isActive();
        super.click();
        boolean expectedActiveState = !wasActive;
        if (isActiveStateReached(expectedActiveState, FIRST_STATE_CHANGE_TIMEOUT)) {
            return;
        }

        // При pageLoadStrategy=none кнопка иногда видима до подключения обработчика Ozon.
        if (isActive() == wasActive) {
            super.click();
        }
        waitUntilActiveState(expectedActiveState, ELEMENT_WAIT_TIMEOUT);
    }

    /** Переключает состояние товара в избранном реальным пользовательским кликом. */
    public void toggleFavoriteState() {
        click();
    }

    public boolean isActive() {
        return isActiveState(waitUntilVisible());
    }

    private boolean isActiveStateReached(
            boolean expectedActiveState,
            Duration stateChangeTimeout) {
        try {
            waitUntilActiveState(expectedActiveState, stateChangeTimeout);
            return true;
        } catch (UIAssertionError firstClickTimeout) {
            return false;
        }
    }

    private void waitUntilActiveState(
            boolean expectedActiveState,
            Duration stateChangeTimeout) {
        element.shouldHave(match(
                String.format(EXPECTED_STATE_DESCRIPTION, expectedActiveState),
                currentElement -> isActiveState(currentElement) == expectedActiveState),
                stateChangeTimeout);
    }

    private static boolean isActiveState(WebElement currentElement) {
        // Карточки и страницы товара используют разные признаки состояния избранного.
        String ariaPressed = currentElement.getAttribute(ARIA_PRESSED_ATTRIBUTE);
        String ariaChecked = currentElement.getAttribute(ARIA_CHECKED_ATTRIBUTE);
        String ariaLabel = currentElement.getAttribute(ARIA_LABEL_ATTRIBUTE);
        String classAttribute = currentElement.getAttribute(CLASS_ATTRIBUTE);
        Boolean ariaLabelState = getAriaLabelState(ariaLabel);

        if (ariaLabelState != null) {
            return ariaLabelState;
        }
        return isTrueAttributeValue(ariaPressed)
                || isTrueAttributeValue(ariaChecked)
                || isActiveClass(classAttribute)
                || isFilledHeartDisplayed(currentElement);
    }

    private static Boolean getAriaLabelState(String ariaLabel) {
        if (ariaLabel == null) {
            return null;
        }
        String normalizedLabel = ariaLabel.toLowerCase(Locale.ROOT);
        if (normalizedLabel.contains(REMOVE_FROM_FAVORITES_LABEL)
                || normalizedLabel.contains(CLEAR_FROM_FAVORITES_LABEL)) {
            return true;
        }
        if (normalizedLabel.contains(ADD_TO_FAVORITES_LABEL)) {
            return false;
        }
        return null;
    }

    private static boolean isActiveClass(String classAttribute) {
        if (classAttribute == null) {
            return false;
        }
        return Arrays.stream(classAttribute.split(CSS_CLASS_SEPARATOR_PATTERN))
                .anyMatch(cssClass -> ACTIVE_CLASS_MARKER.equals(cssClass)
                        || SELECTED_CLASS_MARKER.equals(cssClass));
    }

    private static boolean isFilledHeartDisplayed(WebElement currentElement) {
        return !currentElement.findElements(
                org.openqa.selenium.By.xpath(ACTIVE_SVG_PATH_XPATH)).isEmpty();
    }

    private static boolean isTrueAttributeValue(String attributeValue) {
        return TRUE_ATTRIBUTE_VALUE.equalsIgnoreCase(attributeValue);
    }

    public static HeartIcon byXPath(String xpathExpression) {
        return new HeartIcon($x(xpathExpression));
    }

    public static HeartIcon from(SelenideElement element) {
        return new HeartIcon(element);
    }
}
