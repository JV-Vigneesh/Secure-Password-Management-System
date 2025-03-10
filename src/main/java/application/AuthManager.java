package application;

public class AuthManager {
    private static String loggedInUser;

    public static boolean registerUser(String username, String password) {
        String hashedPassword = PasswordManager.hashPassword(password);
        return DatabaseHelper.registerUser(username, hashedPassword);
    }

    public static boolean authenticateUser(String username, String password) {
        return DatabaseHelper.authenticateUser(username, password);
    }

    public static void setLoggedInUser(String username) {
        loggedInUser = username;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }
}
