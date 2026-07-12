package elements;

import com.codeborne.selenide.SelenideElement;
import core.BaseElement;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Condition.visible;

/**
 * Элемент «иконка сердца» (добавить/убрать из избранного).
 * Умеет: нажимать и сообщать, активна ли иконка (товар в избранном — красная)
 * или неактивна (товар не в избранном — серая).
 */
public class HeartIcon extends BaseElement {

    private HeartIcon(SelenideElement element) {
        super(element);
    }

    public static HeartIcon byXpath(String xpath) {
        return new HeartIcon($x(xpath));
    }

    /** Нажатие на иконку сердца. */
    public void toggle() {
        boolean before = isActive();
        clickHeart();
        long firstAttemptDeadline = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < firstAttemptDeadline) {
            if (isActive() != before) {
                return;
            }
            sleep(200);
        }

        // После eager-навигации кнопка уже видима, но React иногда ещё не
        // подключил обработчик. Повторяем клик только если состояние не изменилось.
        clickHeart();
        waitForState(!before);
    }

    private void clickHeart() {
        element.shouldBe(visible, WAIT);
        executeJavaScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'})", element);
        executeJavaScript("arguments[0].click()", element);
    }

    /**
     * Активна ли иконка (товар добавлен в избранное).
     * Ozon помечает активное состояние через aria-pressed/aria-checked или отдельный класс,
     * поэтому проверяем несколько признаков.
     */
    public boolean isActive() {
        String pressed = element.getAttribute("aria-pressed");
        String checked = element.getAttribute("aria-checked");
        String label = element.getAttribute("aria-label");
        String cls = element.getAttribute("class");
        boolean byAria = "true".equalsIgnoreCase(pressed) || "true".equalsIgnoreCase(checked);
        boolean byLabel = label != null && (label.toLowerCase().contains("удалить из избранного")
                || label.toLowerCase().contains("убрать из избранного"));
        boolean byClass = cls != null && (cls.contains("active") || cls.contains("selected"));
        boolean byFilledHeart = false;
        try {
            String fill = element.$x(".//*[name()='path'][1]").getAttribute("fill");
            byFilledHeart = fill != null && fill.equalsIgnoreCase("#F8104B");
        } catch (Throwable ignored) {
            // У некоторых вариантов кнопки состояние доступно только через aria/class.
        }
        return byAria || byLabel || byClass || byFilledHeart;
    }

    public void waitForState(boolean active) {
        long deadline = System.currentTimeMillis() + WAIT.toMillis();
        while (System.currentTimeMillis() < deadline) {
            if (isActive() == active) {
                return;
            }
            sleep(250);
        }
        throw new AssertionError("Heart state did not become " + active
                + "; aria-label=" + element.getAttribute("aria-label")
                + "; class=" + element.getAttribute("class")
                + "; style=" + element.getAttribute("style")
                + "; color=" + element.getCssValue("color")
                + "; path-fill=" + element.$x(".//*[name()='path'][1]").getAttribute("fill"));
    }
}
