package core;

import com.codeborne.selenide.Selenide;

/** Общая основа Page Object. */
public abstract class BasePage {

    /** Обновляет текущую страницу и возвращает её Page Object. */
    public BasePage refreshPage() {
        Selenide.refresh();
        return this;
    }
}
