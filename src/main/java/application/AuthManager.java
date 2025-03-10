package application;

public class AuthManager {
    private static String loggedInUser = null; // Ensure default is null

    public static boolean registerUser(String username, String password) {
        String hashedPassword = PasswordManager.hashPassword(password);
        return DatabaseHelper.registerUser(username, hashedPassword);
    }

    public static boolean authenticateUser(String username, String password) {
        boolean isAuthenticated = DatabaseHelper.authenticateUser(username, password);
        if (isAuthenticated) {
            setLoggedInUser(username); // Store user on successful login ✅
        }
        return isAuthenticated;
    }

    public static void setLoggedInUser(String username) {
        loggedInUser = username;
        System.out.println("Logged in user: " + loggedInUser); // Debugging ✅
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }
}
