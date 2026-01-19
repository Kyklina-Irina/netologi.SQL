import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import java.sql.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class DeadlineAuthTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/deadline";
    private static final String DB_USER = "app";
    private static final String DB_PASS = "pass";

    @BeforeAll
    static void setUpAll() {
        Configuration.browser = "chrome";
        Configuration.headless = true; // можно false для отладки
        Configuration.timeout = 10000;
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    void shouldLoginWithValidCodeFromDB() throws SQLException {
        String login = "vasya";
        String password = "qwerty123";

        // Получаем код из БД
        String authCode = getAuthCodeFromDB(login);

        // Открываем страницу
        Selenide.open("http://localhost:9999");

        // Вводим логин и пароль
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();

        // Вводим код
        $("[data-test-id=code] input").setValue(authCode);
        $("[data-test-id=action-code]").click();

        // Проверяем успешный вход
        $("h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldBlockAfterThreeWrongAttempts() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id=login] input").setValue("vasya");
        $("[data-test-id=password] input").setValue("wrong");
        for (int i = 0; i < 3; i++) {
            $("[data-test-id=action-login]").click();
            sleep(1000); // небольшая пауза
        }
        $(".notification__content")
                .shouldHave(text("Превышено количество попыток ввода кода"));
    }

    private String getAuthCodeFromDB(String login) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = """
                SELECT ac.code
                FROM auth_codes ac
                JOIN users u ON u.id = ac.user_id
                WHERE u.login = ?
                ORDER BY ac.created DESC
                LIMIT 1
                """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("code");
            } else {
                throw new RuntimeException("Auth code not found for user: " + login);
            }
        }
    }
}