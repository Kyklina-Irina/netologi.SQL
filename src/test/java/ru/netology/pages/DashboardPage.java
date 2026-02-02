package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement dashboardHeader = $("h2");

    public void checkDashboardIsVisible() {
        dashboardHeader.shouldHave(text("Личный кабинет"));
    }
}