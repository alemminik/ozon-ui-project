package core;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

/**
 * Базовый класс всех страниц.
 * Определяет общую логику для всех страниц (например, обновление страницы).
 * Страницы только возвращают данные и выполняют действия, но не делают проверок (assert) —
 * проверки выполняются в тестах.
 */
public abstract class BasePage {

    protected void openLinkInCurrentTab(String xpath) {
        SelenideElement link = $x(xpath).shouldBe(visible);
        String href = link.getAttribute("href");
        if (href == null || href.isBlank()) {
            throw new IllegalStateException("Link has no href: " + xpath);
        }
        open(href);
    }

    /** Обновление текущей страницы. */
    public void refreshPage() {
        Selenide.refresh();
    }
}
