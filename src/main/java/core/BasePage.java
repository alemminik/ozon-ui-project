package core;

import com.codeborne.selenide.Selenide;

public abstract class BasePage {
    // Общий метод для обновления любой страницы
    public void refreshPage() {
        Selenide.refresh();
    }
}