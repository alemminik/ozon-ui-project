package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый класс всех тестов.
 * Выполняет изначальную настройку браузера, авторизацию и закрытие браузера.
 * Тесты наследуются от него и вызывают только методы страниц.
 *
 * Логика авторизации вынесена в отдельный сервис {@link AuthService}
 * (по совету из статьи — чтобы не «захламлять» базовый класс теста).
 */
public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    private final AuthService authService = new AuthService();

    @BeforeEach
    public void setUp() {
        log.info("Настройка браузера перед тестом");
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        Configuration.timeout = 15000;
        Configuration.headless = false; // Ozon блокирует headless-режим

        // Настройки браузера с постоянным профилем (хранит сессию авторизации)
        Configuration.browserCapabilities = AuthService.browserOptions();

        // Гарантируем авторизацию (не считается действием теста)
        authService.login();
    }

    @AfterEach
    public void tearDown() {
        log.info("Закрытие браузера после теста");
        Selenide.closeWebDriver();
    }
}
