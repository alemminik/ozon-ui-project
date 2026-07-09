package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    @BeforeEach
    public void setUp() {
        // Настройки перед каждым тестом
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager"; // Не ждем загрузки тяжелых картинок Ozon
    }

    @AfterEach
    public void tearDown() {
        // Закрываем браузер после каждого теста
        Selenide.closeWebDriver();
    }
}