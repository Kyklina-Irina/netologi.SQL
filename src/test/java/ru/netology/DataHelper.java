package ru.netology;

public class DataHelper {

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        try {
            String code = DBHelper.getVerificationCode(authInfo.getLogin());
            return new VerificationCode(code);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить код подтверждения", e);
        }
    }

    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() { return login; }
        public String getPassword() { return password; }
    }

    public static class VerificationCode {
        private final String code;

        public VerificationCode(String code) {
            this.code = code;
        }

        public String getCode() { return code; }
    }
}