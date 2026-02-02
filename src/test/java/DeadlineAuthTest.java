package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.*;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;
import ru.netology.pages.DashboardPage;

import static com.codeborne.selenide.Selenide.open;

public class DeadlineAuthTest {

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999"); // ТОЛЬКО открытие страницы, БЕЗ очистки БД!
    }

    @Test
    void shouldLoginWithValidCodeFromDB() {
        var authInfo = DataHelper.getAuthInfo();
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);

        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode.getCode());

        dashboardPage.checkDashboardIsVisible();
    }

    @Test
    void shouldBlockAfterThreeWrongAttempts() {
        var loginPage = new LoginPage();

        for (int i = 0; i < 3; i++) {
            loginPage.invalidLogin("vasya", "wrong");
        }

        loginPage.checkBlockedMessage();
    }
}