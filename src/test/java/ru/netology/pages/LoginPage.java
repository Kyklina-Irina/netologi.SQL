package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorMessage = $("[data-test-id=error-notification]");
    private SelenideElement blockedMessage = $(".notification__content");

    public VerificationPage validLogin(ru.netology.DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public LoginPage invalidLogin(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
        errorMessage.shouldBe(visible);
        return this;
    }

    public void checkBlockedMessage() {
        blockedMessage.shouldHave(com.codeborne.selenide.Condition.text("Превышено количество попыток ввода кода"));
    }
}