package core;

import elements.VisibleElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

/** Открывает Ozon и обеспечивает авторизованную сессию в постоянном профиле Chrome. */
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private static final String BASE_URL = "https://www.ozon.ru";
    private static final String PROFILE_LINK_XPATH = "//a[contains(@href, '/my/main')]";
    private static final String CURRENT_DIRECTORY_PROPERTY_NAME = "user.dir";
    private static final String USER_PROFILE_DIRECTORY_NAME = "chrome-profile";
    private static final String USER_DATA_DIRECTORY_ARGUMENT_PREFIX = "--user-data-dir=";
    private static final String DISABLE_AUTOMATION_ARGUMENT =
            "--disable-blink-features=AutomationControlled";
    private static final String RUSSIAN_LANGUAGE_ARGUMENT = "--lang=ru-RU";
    private static final String ALREADY_LOGGED_IN_MESSAGE =
            "Пользователь уже авторизован в профиле браузера.";
    private static final String MANUAL_LOGIN_REQUIRED_MESSAGE =
            "Выполните ручной вход в открытом окне. Ожидание входа до двух минут.";
    private static final String LOGIN_COMPLETED_MESSAGE =
            "Вход выполнен, сессия сохранена в профиле браузера.";
    private static final Duration MANUAL_LOGIN_TIMEOUT = Duration.ofMinutes(2);
    private static final String USER_DATA_DIRECTORY = new File(
            System.getProperty(CURRENT_DIRECTORY_PROPERTY_NAME),
            USER_PROFILE_DIRECTORY_NAME).getAbsolutePath();

    private final VisibleElement profileLink = VisibleElement.byXPath(PROFILE_LINK_XPATH);

    /** Открывает главную страницу и ожидает ручной вход, если сохранённая сессия отсутствует. */
    public void openHomePageAndEnsureUserLoggedIn() {
        open(BASE_URL);

        if (isUserLoggedIn()) {
            LOGGER.info(ALREADY_LOGGED_IN_MESSAGE);
            return;
        }

        LOGGER.warn(MANUAL_LOGIN_REQUIRED_MESSAGE);
        waitUntilUserLoggedIn();
        LOGGER.info(LOGIN_COMPLETED_MESSAGE);
    }

    private boolean isUserLoggedIn() {
        return profileLink.isPresent();
    }

    private void waitUntilUserLoggedIn() {
        profileLink.waitUntilDisplayed(MANUAL_LOGIN_TIMEOUT);
    }

    /** Возвращает настройки Chrome для повторного использования сохранённой сессии. */
    public static ChromeOptions createBrowserOptions() {
        return new ChromeOptions()
                .addArguments(USER_DATA_DIRECTORY_ARGUMENT_PREFIX + USER_DATA_DIRECTORY)
                .addArguments(DISABLE_AUTOMATION_ARGUMENT)
                .addArguments(RUSSIAN_LANGUAGE_ARGUMENT);
    }
}
