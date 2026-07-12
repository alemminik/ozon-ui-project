package core;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Сервис авторизации на Ozon.
 *
 * Ozon пускает только по коду из SMS и защищается капчей, поэтому автоматический вход
 * невозможен. Используется постоянный профиль браузера: первый вход выполняется вручную
 * (см. README), сессия сохраняется в папку chrome-profile и переиспользуется дальше.
 * Метод {@link #login()} не считается действием теста — это подготовка окружения.
 */
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private static final String BASE_URL = "https://www.ozon.ru";

    /** Признак того, что пользователь авторизован (в шапке есть ссылка на личный кабинет). */
    private static final String PROFILE_LINK = "//a[contains(@href, '/my/main')]";

    /** Папка с постоянным профилем Chrome (хранит сессию авторизации между запусками). */
    public static final String USER_DATA_DIR =
            new File(System.getProperty("user.dir"), "chrome-profile").getAbsolutePath();

    /**
     * Настройки браузера, при которых сохраняется сессия авторизации
     * и снижается вероятность блокировки как «робота».
     */
    public static ChromeOptions browserOptions() {
        return new ChromeOptions()
                .addArguments("--user-data-dir=" + USER_DATA_DIR)
                .addArguments("--disable-blink-features=AutomationControlled")
                .addArguments("--lang=ru-RU");
    }

    /**
     * Гарантирует, что пользователь авторизован.
     * Если сессия уже сохранена в профиле — просто открывает сайт.
     * Если нет — открывает сайт и ждёт, пока вход будет выполнен вручную
     * (номер телефона + код из SMS + капча).
     */
    public void login() {
        open(BASE_URL);

        if (isLoggedIn()) {
            log.info("Пользователь уже авторизован (сессия из профиля браузера).");
            return;
        }

        log.warn("Пользователь не авторизован. Войдите вручную в открытом окне браузера: "
                + "телефон + код из SMS. Ожидание входа до 2 минут...");
        waitForManualLogin();
    }

    /** Проверка, авторизован ли пользователь. */
    public boolean isLoggedIn() {
        try {
            return $x(PROFILE_LINK).isDisplayed();
        } catch (Throwable e) {
            return false;
        }
    }

    /** Ожидание ручного входа (до 2 минут), после которого сессия сохранится в профиле. */
    private void waitForManualLogin() {
        int waitedSeconds = 0;
        int maxSeconds = 120;
        while (waitedSeconds < maxSeconds) {
            if (isLoggedIn()) {
                log.info("Вход выполнен, сессия сохранена в профиль браузера.");
                return;
            }
            sleep(5000);
            waitedSeconds += 5;
        }
        log.warn("Вход так и не выполнен за отведённое время. "
                + "Тест продолжится, но может упасть из-за отсутствия авторизации.");
    }
}
